package SPITEC.CategoriaMaterial.Entity;


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
@Table(name = "categoria_material")
public class CategoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_CATEGORIA_GEN")
    @SequenceGenerator(name = "SEC_CATEGORIA_GEN", sequenceName = "SEC_CATEGORIA", allocationSize = 1)
    @Column(name = "idCategoria")
    private Long id;
    @Column(name = "nombreCategoria", unique = true, nullable = false)
    private String nombreCategoria;
}
