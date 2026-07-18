package SPITEC.Materiales.Entity;


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
@Table(name = "material")
public class MaterialesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_MATERIAL_GEN")
    @SequenceGenerator(name = "SEC_MATERIAL_GEN", sequenceName = "SEC_MATERIAL", allocationSize = 1)
    @Column(name = "idMaterial")
    private Long id;
    @Column(name = "nombreMaterial", nullable = false)
    private String nombreMaterial;
    @Lob
    @Column(name = "descripcionMaterial")
    private String descripcionMaterial;
    @Column(name = "idCategoria")
    private Long idCategoria;
    @Column(name = "idArea")
    private Long idArea;
}
