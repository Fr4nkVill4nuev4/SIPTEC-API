package SPITEC.Inventario.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "inventario")
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_INVENTARIO_GEN")
    @SequenceGenerator(name = "SEC_INVENTARIO_GEN", sequenceName = "SEC_INVENTARIO", allocationSize = 1)
    @Column(name = "idInventario")
    private Long id;
    @Column(name = "idMaterial", nullable = false)
    private Long idMaterial;
    @Column(name = "codigoInventario", unique = true)
    private String codigoInventario;
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_adquisicion")
    private String fechaAdquisicion;
}
