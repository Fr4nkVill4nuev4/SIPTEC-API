package SPITEC.Prestamo.Entity;


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
@Table(name = "prestamo")
public class PrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_PRESTAMO_GEN")
    @SequenceGenerator(name = "SEC_PRESTAMO_GEN", sequenceName = "SEC_PRESTAMO", allocationSize = 1)
    @Column(name = "idPrestamo")
    private Long id;
    @Column(name = "idUsuario")
    private Long idUsuario;
    @Column(name = "fechaInicio", nullable = false)
    private String fechaInicio;
    @Column(name = "fechaFin", nullable = false)
    private String fechaFin;
    @Column(name = "estado")
    private Long estado;
    @Column(name = "creadoEn")
    private String creadoEn;
}
