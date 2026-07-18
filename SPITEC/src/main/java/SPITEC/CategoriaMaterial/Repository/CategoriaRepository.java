package SPITEC.CategoriaMaterial.Repository;

import SPITEC.CategoriaMaterial.Entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    Optional<CategoriaEntity> findByNombreCategoria(String nombreCategoria);
}
