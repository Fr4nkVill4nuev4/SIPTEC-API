package SPITEC.Instituciones.Repository;

import SPITEC.Instituciones.Entity.InstitucionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitucionesRepository extends JpaRepository<InstitucionesEntity, Long> {
    Optional<InstitucionesEntity> findByNombreInstitucion(String nombreInstitucion);
}
