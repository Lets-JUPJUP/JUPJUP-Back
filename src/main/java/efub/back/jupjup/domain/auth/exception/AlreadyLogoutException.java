package efub.back.jupjup.domain.auth.exception;

import efub.back.jupjup.domain.security.exception.UnAuthorizedTokenException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyLogoutException extends UnAuthorizedTokenException {
}
