package SPITEC.Returns.Repository;

import SPITEC.Returns.Entity.ReturnsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnsRepository extends JpaRepository<ReturnsEntity, Long> {
}
