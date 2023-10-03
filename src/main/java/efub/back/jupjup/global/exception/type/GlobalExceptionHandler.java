package efub.back.jupjup.global.exception.type;

import efub.back.jupjup.global.exception.ExceptionType;
import efub.back.jupjup.global.exception.custom.ApplicationException;
import efub.back.jupjup.global.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        log.info(String.format("Application Exception : %s", e));
        final ErrorResponse response = ErrorResponse.builder()
                .status(e.getHttpStatus())
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> runtimeException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        log.error(String.format("UnHandled Exception : %s\n" + "%s:%s:%s", e, stackTrace[0].getClassName(),
                stackTrace[0].getMethodName(), stackTrace[0].getLineNumber()), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ExceptionType.UNHANDLED_EXCEPTION.getMessage();
        String errorCode = ExceptionType.UNHANDLED_EXCEPTION.getErrorCode();
        return ResponseEntity.internalServerError().body(new ErrorResponse(status, errorCode, message));
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info(String.format("MethodArgumentNotValidException : %s", e));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getErrorCode();
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(status, errorCode, message));
    }
}
