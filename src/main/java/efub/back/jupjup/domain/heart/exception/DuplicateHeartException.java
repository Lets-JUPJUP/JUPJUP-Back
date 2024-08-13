package efub.back.jupjup.domain.heart.exception;

import efub.back.jupjup.global.exception.custom.ApplicationException;
import org.springframework.http.HttpStatus;

public class DuplicateHeartException extends ApplicationException {
	public DuplicateHeartException() {
		super(HttpStatus.BAD_REQUEST);
	}
}
