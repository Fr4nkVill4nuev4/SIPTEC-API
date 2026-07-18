package SPITEC.Instituciones.Entity;


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
@Table(name = "instituciones")
public class InstitucionesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_INSTITUCIONES_GEN")
    @SequenceGenerator(name = "SEC_INSTITUCIONES_GEN", sequenceName = "SEC_INSTITUCIONES", allocationSize = 1)
    @Column(name = "idInstitucion")
    private Long id;
    @Column(name = "nombreInstitucion", unique = true, nullable = false)
    private String nombreInstitucion;
}
