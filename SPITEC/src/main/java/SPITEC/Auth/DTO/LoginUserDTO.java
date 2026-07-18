package SPITEC.Auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LoginUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String institution;
    private Boolean active;
}
