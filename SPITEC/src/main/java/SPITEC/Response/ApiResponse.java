package SPITEC.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@AllArgsConstructor
public class ApiResponse<T> {
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private boolean success;
    private String message;
    private T data;
}
