package SPITEC.Reportes.Repository;

import SPITEC.Reportes.Entity.ReportesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ReportesRepository extends JpaRepository<ReportesEntity, Long> {
    List<ReportesEntity> findByGeneradoPorOrderByIdDesc(Long generadoPor); List<ReportesEntity> findAllByOrderByIdDesc();
}
