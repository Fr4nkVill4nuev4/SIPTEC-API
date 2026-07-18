package SPITEC.Returns.Entity;

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
@Table(name = "returns")
public class ReturnsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEC_RETURNS_GEN")
    @SequenceGenerator(name = "SEC_RETURNS_GEN", sequenceName = "SEC_RETURNS", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "prestamoId")
    private Long prestamoId;
    @Column(name = "fechaDevolucion")
    private String fechaDevolucion;
    @Column(name = "observacion")
    private String observacion;
}
