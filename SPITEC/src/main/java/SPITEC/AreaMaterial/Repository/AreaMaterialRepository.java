package SPITEC.AreaMaterial.Repository;

import SPITEC.AreaMaterial.Entity.AreaMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaMaterialRepository extends JpaRepository<AreaMaterialEntity, Long> {
    Optional<AreaMaterialEntity> findByNombreArea(String nombreArea);
}
