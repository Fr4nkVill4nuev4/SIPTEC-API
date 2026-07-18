package SPITEC.Usuarios.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuariosDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String institution;
    private Boolean active;
}
