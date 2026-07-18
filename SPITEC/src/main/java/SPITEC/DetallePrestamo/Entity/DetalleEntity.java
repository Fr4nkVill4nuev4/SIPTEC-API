package SPITEC.DetallePrestamo.Entity;


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
@Table(name = "detalle_prestamo")
public class DetalleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_DETALLE_GEN")
    @SequenceGenerator(name = "SEC_DETALLE_GEN", sequenceName = "SEC_DETALLE", allocationSize = 1)
    @Column(name = "idDetalle")
    private Long id;
    @Column(name = "idPrestamo")
    private Long idPrestamo;
    @Column(name = "idInventario")
    private Long idInventario;
    @Column(name = "cantidad")
    private Integer cantidad;
}
