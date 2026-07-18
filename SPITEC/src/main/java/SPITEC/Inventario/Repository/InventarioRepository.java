package SPITEC.Inventario.Repository;

import SPITEC.Inventario.Entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {
    Optional<InventarioEntity> findByCodigoInventario(String codigoInventario);
}
