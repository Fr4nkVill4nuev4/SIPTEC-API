package SPITEC.Reportes.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "reportes")
public class ReportesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_REPORTES_GEN")
    @SequenceGenerator(name = "SEC_REPORTES_GEN", sequenceName = "SEC_REPORTES", allocationSize = 1)
    @Column(name = "idReporte")
    private Long id;
    @Column(name = "tipoReporte", nullable = false)
    private String tipoReporte;
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    @Lob
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "generadoPor")
    private Long generadoPor;
    @Column(name = "estado")
    private String estado;
    @Column(name = "creadoEn")
    private String creadoEn;
}
