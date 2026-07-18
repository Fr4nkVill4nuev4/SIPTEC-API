package SPITEC.EstadoPrestamo.Controller;

import SPITEC.EstadoPrestamo.DTO.EstadoDTO;
import SPITEC.EstadoPrestamo.Service.EstadoService;
import SPITEC.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/estado")
@CrossOrigin
public class EstadoController {
    private final EstadoService service;

    public EstadoController(EstadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EstadoDTO>> guardar(@Valid @RequestBody EstadoDTO json) {
        try {
            EstadoDTO dto = service.nuevoEstado(json);
            if (dto != null) {
                log.info("Datos ingresados exitosamente");
                return ResponseEntity.ok(new ApiResponse<>(true, "Datos ingresados exitosamente", dto));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "El proceso no se pudo completar", json));
        } catch (Exception e) {
            log.error("El proceso no se pudo completar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "El proceso no se pudo completar", json));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EstadoDTO>>> obtenerTodo() {
        List<EstadoDTO> data = service.obtenerTodo();
        return ResponseEntity.ok(new ApiResponse<>(true, "Datos encontrados exitosamente", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstadoDTO>> buscarPorId(@PathVariable Long id) {
        EstadoDTO data = service.buscarPorId(id);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontraron datos", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Datos encontrados exitosamente", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EstadoDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoDTO json) {
        EstadoDTO data = service.actualizar(id, json);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se pudo actualizar el registro", json));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Datos actualizados exitosamente", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminarData(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontraron datos", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Datos eliminados exitosamente", null));
    }
}
