package SPITEC.Auth.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthDTO {
    private Long id;
    private Long usuarioId;
    private String token;
    private Boolean activo;
}
