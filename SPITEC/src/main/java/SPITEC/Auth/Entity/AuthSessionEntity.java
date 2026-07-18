package SPITEC.Auth.Entity;


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
@Table(name = "auth_session")
public class AuthSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_AUTH_SESSION_GEN")
    @SequenceGenerator(name = "SEC_AUTH_SESSION_GEN", sequenceName = "SEC_AUTH_SESSION", allocationSize = 1)
    @Column(name = "idSession")
    private Long id;
    @Column(name = "token", unique = true, nullable = false)
    private String token;
    @Column(name = "userId", nullable = false)
    private Long userId;
    @Column(name = "expiresAt", nullable = false)
    private Long expiresAt;
}
