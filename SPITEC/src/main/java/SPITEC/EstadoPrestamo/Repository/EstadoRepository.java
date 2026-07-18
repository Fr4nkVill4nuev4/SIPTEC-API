package SPITEC.EstadoPrestamo.Repository;

import SPITEC.EstadoPrestamo.Entity.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoEntity, Long> {
    Optional<EstadoEntity> findByNombreEstado(String nombreEstado);
}
