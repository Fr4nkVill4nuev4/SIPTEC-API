package SPITEC.Inventario.Service;

import SPITEC.AreaMaterial.Entity.AreaMaterialEntity;
import SPITEC.AreaMaterial.Repository.AreaMaterialRepository;
import SPITEC.CategoriaMaterial.Entity.CategoriaEntity;
import SPITEC.CategoriaMaterial.Repository.CategoriaRepository;
import SPITEC.Exception.AppException;
import SPITEC.Inventario.DTO.InventarioDTO;
import SPITEC.Inventario.Entity.InventarioEntity;
import SPITEC.Inventario.Repository.InventarioRepository;
import SPITEC.Materiales.Entity.MaterialesEntity;
import SPITEC.Materiales.Repository.MaterialesRepository;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {
    private final InventarioRepository repo;
    private final MaterialesRepository materialesRepository;
    private final CategoriaRepository categoriaRepository;
    private final AreaMaterialRepository areaRepository;
    private final RolesRepository rolesRepository;

    public InventarioService(InventarioRepository repo, MaterialesRepository materialesRepository, CategoriaRepository categoriaRepository, AreaMaterialRepository areaRepository, RolesRepository rolesRepository) {
        this.repo = repo;
        this.materialesRepository = materialesRepository;
        this.categoriaRepository = categoriaRepository;
        this.areaRepository = areaRepository;
        this.rolesRepository = rolesRepository;
    }

    public List<InventarioDTO> getInventory(UsuariosEntity user) {
        return repo.findAll().stream()
                .filter(item -> !isEmployee(user) || "Disponible".equals(item.getEstado()))
                .map(this::toDTO)
                .toList();
    }

    public InventarioDTO createInventoryItem(InventarioDTO dto) {
        String code = TextUtils.cleanText(dto.getCode());
        String name = TextUtils.cleanText(dto.getName());
        if (code.isEmpty() || name.isEmpty()) throw new AppException("Codigo y nombre son obligatorios.", 400);
        if (repo.findByCodigoInventario(code).isPresent()) throw new AppException("Ya existe un producto con ese codigo.", 409);
        Long materialId = upsertMaterial(name, dto.getDescription(), dto.getCategory(), dto.getLocation());
        InventarioEntity entity = new InventarioEntity();
        entity.setIdMaterial(materialId);
        entity.setCodigoInventario(code);
        entity.setEstado("Disponible");
        entity.setFechaAdquisicion(TextUtils.cleanText(dto.getAcquiredAt()).isEmpty() ? LocalDate.now().toString() : dto.getAcquiredAt());
        return toDTO(repo.save(entity));
    }

    public InventarioDTO updateInventoryItem(Long id, InventarioDTO dto) {
        InventarioEntity entity = repo.findById(id).orElseThrow(() -> new AppException("Producto no encontrado.", 404));
        String code = TextUtils.cleanText(dto.getCode());
        String name = TextUtils.cleanText(dto.getName());
        String status = TextUtils.cleanText(dto.getStatus()).isEmpty() ? "Disponible" : TextUtils.cleanText(dto.getStatus());
        if (code.isEmpty() || name.isEmpty()) throw new AppException("Codigo y nombre son obligatorios.", 400);
        if (!List.of("Disponible", "Prestado", "Daniado").contains(status)) throw new AppException("Estado de inventario no valido.", 400);
        repo.findByCodigoInventario(code).ifPresent(existing -> {
            if (!existing.getId().equals(id)) throw new AppException("Ya existe un producto con ese codigo.", 409);
        });
        updateMaterial(entity.getIdMaterial(), name, dto.getDescription(), dto.getCategory(), dto.getLocation());
        entity.setCodigoInventario(code);
        entity.setEstado(status);
        entity.setFechaAdquisicion(TextUtils.cleanText(dto.getAcquiredAt()).isEmpty() ? LocalDate.now().toString() : dto.getAcquiredAt());
        return toDTO(repo.save(entity));
    }

    public InventarioDTO cycleInventoryStatus(Long id) {
        InventarioEntity entity = repo.findById(id).orElseThrow(() -> new AppException("Producto no encontrado.", 404));
        List<String> order = List.of("Disponible", "Prestado", "Daniado");
        entity.setEstado(order.get((order.indexOf(entity.getEstado()) + 1) % order.size()));
        return toDTO(repo.save(entity));
    }

    public void deleteInventoryItem(Long id) {
        if (!repo.existsById(id)) throw new AppException("Producto no encontrado.", 404);
        repo.deleteById(id);
    }

    public boolean canManageInventory(UsuariosEntity user) {
        return List.of("ADMINISTRADOR", "IT").contains(getRole(user));
    }

    public InventarioDTO toDTO(InventarioEntity entity) {
        MaterialesEntity material = materialesRepository.findById(entity.getIdMaterial()).orElse(null);
        CategoriaEntity category = material == null || material.getIdCategoria() == null ? null : categoriaRepository.findById(material.getIdCategoria()).orElse(null);
        AreaMaterialEntity area = material == null || material.getIdArea() == null ? null : areaRepository.findById(material.getIdArea()).orElse(null);
        InventarioDTO dto = new InventarioDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCodigoInventario());
        dto.setName(material == null ? "" : material.getNombreMaterial());
        dto.setDescription(material == null ? "" : material.getDescripcionMaterial());
        dto.setCategory(category == null ? "" : category.getNombreCategoria());
        dto.setLocation(area == null ? "" : area.getNombreArea());
        dto.setStatus(entity.getEstado());
        dto.setAcquiredAt(entity.getFechaAdquisicion());
        dto.setMaterialId(entity.getIdMaterial());
        return dto;
    }

    private Long upsertMaterial(String name, String description, String category, String area) {
        Long categoryId = getOrCreateCategory(category).getId();
        Long areaId = getOrCreateArea(area).getId();
        return materialesRepository.findByNombreMaterialAndIdCategoriaAndIdArea(name, categoryId, areaId)
                .map(MaterialesEntity::getId)
                .orElseGet(() -> {
                    MaterialesEntity material = new MaterialesEntity();
                    material.setNombreMaterial(name);
                    material.setDescripcionMaterial(TextUtils.cleanText(description));
                    material.setIdCategoria(categoryId);
                    material.setIdArea(areaId);
                    return materialesRepository.save(material).getId();
                });
    }

    private void updateMaterial(Long id, String name, String description, String category, String area) {
        MaterialesEntity material = materialesRepository.findById(id).orElse(new MaterialesEntity());
        material.setNombreMaterial(name);
        material.setDescripcionMaterial(TextUtils.cleanText(description));
        material.setIdCategoria(getOrCreateCategory(category).getId());
        material.setIdArea(getOrCreateArea(area).getId());
        materialesRepository.save(material);
    }

    private CategoriaEntity getOrCreateCategory(String value) {
        String name = TextUtils.cleanText(value).isEmpty() ? "Material de apoyo" : TextUtils.cleanText(value);
        return categoriaRepository.findByNombreCategoria(name).orElseGet(() -> {
            CategoriaEntity entity = new CategoriaEntity();
            entity.setNombreCategoria(name);
            return categoriaRepository.save(entity);
        });
    }

    private AreaMaterialEntity getOrCreateArea(String value) {
        String name = TextUtils.cleanText(value).isEmpty() ? "Bodega tecnica" : TextUtils.cleanText(value);
        return areaRepository.findByNombreArea(name).orElseGet(() -> {
            AreaMaterialEntity entity = new AreaMaterialEntity();
            entity.setNombreArea(name);
            return areaRepository.save(entity);
        });
    }

    private boolean isEmployee(UsuariosEntity user) {
        return "EMPLEADO".equals(getRole(user));
    }

    private String getRole(UsuariosEntity user) {
        return rolesRepository.findById(user.getIdRol()).map(item -> item.getNombreRol()).orElse("");
    }
}
