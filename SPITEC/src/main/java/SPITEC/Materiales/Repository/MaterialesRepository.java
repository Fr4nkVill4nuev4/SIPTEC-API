package SPITEC.Materiales.Repository;

import SPITEC.Materiales.Entity.MaterialesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialesRepository extends JpaRepository<MaterialesEntity, Long> {
    Optional<MaterialesEntity> findByNombreMaterialAndIdCategoriaAndIdArea(String nombreMaterial, Long idCategoria, Long idArea);
}
