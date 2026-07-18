package SPITEC.Reportes.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportesDTO {
    private Long id;
    private String type;
    private String title;
    private String description;
    private String content;
    private String status;
    private String createdAt;
    private String author;
}
