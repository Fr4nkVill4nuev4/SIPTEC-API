package SPITEC.Usuarios.Entity;


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
@Table(name = "usuarios")
public class UsuariosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_USUARIOS_GEN")
    @SequenceGenerator(name = "SEC_USUARIOS_GEN", sequenceName = "SEC_USUARIOS", allocationSize = 1)
    @Column(name = "idUsuario")
    private Long id;
    @Column(name = "nombreUsuario", nullable = false)
    private String nombreUsuario;
    @Column(name = "apellidoUsuario", nullable = false)
    private String apellidoUsuario;
    @Column(name = "correoUsuario", unique = true, nullable = false)
    private String correoUsuario;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "idRol", nullable = false)
    private Long idRol;
    @Column(name = "idInstitucion", nullable = false)
    private Long idInstitucion;
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    @Column(name = "creadoEn")
    private String creadoEn;
}
