package SPITEC.Inventario.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventarioDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private String location;
    private String status;
    private String acquiredAt;
    private Long materialId;
}
