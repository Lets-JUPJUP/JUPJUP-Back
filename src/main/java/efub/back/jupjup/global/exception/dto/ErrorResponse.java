package efub.back.jupjup.global.exception.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
    private HttpStatus status;
    private String errorCode;
    private String message;

    @Builder
    public ErrorResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.errorCode = code;
        this.message = message;
    }
}
