package SPITEC.Roles.Entity;


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
@Table(name = "roles")
public class RolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_ROLES_GEN")
    @SequenceGenerator(name = "SEC_ROLES_GEN", sequenceName = "SEC_ROLES", allocationSize = 1)
    @Column(name = "idRol")
    private Long id;
    @Column(name = "nombreRol", unique = true, nullable = false)
    private String nombreRol;
}
