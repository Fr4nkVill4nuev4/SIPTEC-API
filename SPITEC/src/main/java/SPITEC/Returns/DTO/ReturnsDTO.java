package SPITEC.Returns.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReturnsDTO {
    private Long id;
    private String student;
    private String item;
    private String code;
    private String status;
    private String dueDate;
    private String action;
    private String damageDescription;
}
