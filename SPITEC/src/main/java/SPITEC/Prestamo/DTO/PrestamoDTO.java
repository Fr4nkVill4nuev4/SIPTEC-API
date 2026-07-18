package SPITEC.Prestamo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PrestamoDTO {
    private Long id;
    private String code;
    private String student;
    private String date;
    private String item;
    private String state;
    private Long inventoryId;
    private Long userId;
    private String startDate;
    private String endDate;
}
