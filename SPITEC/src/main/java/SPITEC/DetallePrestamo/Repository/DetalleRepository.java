package SPITEC.DetallePrestamo.Repository;

import SPITEC.DetallePrestamo.Entity.DetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleRepository extends JpaRepository<DetalleEntity, Long> {
    Optional<DetalleEntity> findFirstByIdPrestamo(Long idPrestamo);
}
