package efub.back.jupjup.domain.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestTokenDto {
	private String token;

	public TestTokenDto(String token) {
		this.token = token;
	}
}
