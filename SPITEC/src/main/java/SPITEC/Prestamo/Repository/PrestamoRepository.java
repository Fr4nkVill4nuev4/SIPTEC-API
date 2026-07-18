package SPITEC.Prestamo.Repository;

import SPITEC.Prestamo.Entity.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Long> {
    List<PrestamoEntity> findByIdUsuario(Long idUsuario);
}
