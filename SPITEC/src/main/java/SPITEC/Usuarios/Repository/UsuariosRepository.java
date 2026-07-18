package SPITEC.Usuarios.Repository;

import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Long> {
    Optional<UsuariosEntity> findByCorreoUsuario(String correoUsuario);
}
