package SPITEC.Permiso.Entity;


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
@Table(name = "permiso")
public class PermisosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_PERMISO_GEN")
    @SequenceGenerator(name = "SEC_PERMISO_GEN", sequenceName = "SEC_PERMISO", allocationSize = 1)
    @Column(name = "idPermiso")
    private Long id;
    @Column(name = "nombrePermiso", unique = true, nullable = false)
    private String nombrePermiso;
}
