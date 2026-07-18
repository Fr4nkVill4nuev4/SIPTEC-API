package SPITEC.Auth.Repository;

import SPITEC.Auth.Entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSessionEntity, Long> {
    Optional<AuthSessionEntity> findByToken(String token); void deleteByToken(String token);
}
