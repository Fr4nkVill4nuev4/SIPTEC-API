package SPITEC.Historial.Repository;

import SPITEC.Historial.Entity.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {
    List<HistorialEntity> findByIdUsuarioOrderByIdDesc(Long idUsuario); List<HistorialEntity> findAllByOrderByIdDesc();
}
