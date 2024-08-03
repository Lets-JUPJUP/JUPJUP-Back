package efub.back.jupjup.domain.post.dto;

import java.util.List;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadRequestDto {
	private List<String> imageList;
}
