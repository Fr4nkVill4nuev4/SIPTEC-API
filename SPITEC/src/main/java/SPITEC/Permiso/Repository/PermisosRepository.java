package SPITEC.Permiso.Repository;

import SPITEC.Permiso.Entity.PermisosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisosRepository extends JpaRepository<PermisosEntity, Long> {
    Optional<PermisosEntity> findByNombrePermiso(String nombrePermiso);
}
