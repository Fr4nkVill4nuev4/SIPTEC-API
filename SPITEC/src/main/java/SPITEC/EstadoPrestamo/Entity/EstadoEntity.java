package SPITEC.EstadoPrestamo.Entity;


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
@Table(name = "estado_prestamo")
public class EstadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_ESTADO_PRESTAMO_GEN")
    @SequenceGenerator(name = "SEC_ESTADO_PRESTAMO_GEN", sequenceName = "SEC_ESTADO_PRESTAMO", allocationSize = 1)
    @Column(name = "idEstado")
    private Long id;
    @Column(name = "nombreEstado", unique = true, nullable = false)
    private String nombreEstado;
}
