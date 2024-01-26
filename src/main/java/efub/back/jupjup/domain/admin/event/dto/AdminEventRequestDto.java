package efub.back.jupjup.domain.admin.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminEventRequestDto {
	private String title;
	private String infoUrl;
	private String imageUrl;
}
