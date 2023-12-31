package efub.back.jupjup.global.exception.custom;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApplicationException{
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String optionalMessage) {
        super(HttpStatus.FORBIDDEN, optionalMessage);
    }
}
