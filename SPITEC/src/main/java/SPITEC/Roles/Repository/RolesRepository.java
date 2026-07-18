package SPITEC.Roles.Repository;

import SPITEC.Roles.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByNombreRol(String nombreRol);
}
