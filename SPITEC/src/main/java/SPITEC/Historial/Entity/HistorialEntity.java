package SPITEC.Historial.Entity;


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
@Table(name = "historial")
public class HistorialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_HISTORIAL_GEN")
    @SequenceGenerator(name = "SEC_HISTORIAL_GEN", sequenceName = "SEC_HISTORIAL", allocationSize = 1)
    @Column(name = "idHistorial")
    private Long id;
    @Column(name = "idInventario")
    private Long idInventario;
    @Column(name = "idUsuario")
    private Long idUsuario;
    @Column(name = "codigoInventario")
    private String codigoInventario;
    @Column(name = "nombreMaterial")
    private String nombreMaterial;
    @Column(name = "fechaInicio")
    private String fechaInicio;
    @Column(name = "fechaFin")
    private String fechaFin;
    @Column(name = "nombreUsuario")
    private String nombreUsuario;
    @Column(name = "estado")
    private String estado;
    @Column(name = "creadoEn")
    private String creadoEn;
}
