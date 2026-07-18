package SPITEC.Historial.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistorialDTO {
    private String code;
    private String item;
    private String start;
    private String end;
    private String user;
    private String status;
}
