package efub.back.jupjup.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUploadResponseDto {
	private String rawFile;
	private String fileName;
	private String presignedUrl;

}
