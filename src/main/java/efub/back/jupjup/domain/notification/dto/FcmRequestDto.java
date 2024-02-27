package efub.back.jupjup.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FcmRequestDto {
	private String targetToken;
	private String title;
	private String body;
}
