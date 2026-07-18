package SPITEC.AreaMaterial.Entity;


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
@Table(name = "area_material")
public class AreaMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_AREA_GEN")
    @SequenceGenerator(name = "SEC_AREA_GEN", sequenceName = "SEC_AREA", allocationSize = 1)
    @Column(name = "idArea")
    private Long id;
    @Column(name = "nombreArea", unique = true, nullable = false)
    private String nombreArea;
}
