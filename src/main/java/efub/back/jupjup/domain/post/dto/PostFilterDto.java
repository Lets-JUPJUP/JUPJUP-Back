package efub.back.jupjup.domain.post.dto;

import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.PostGender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFilterDto {
	private PostGender postGender;
	private Boolean withPet;
	private District district;
	private Integer minAge;
	private Integer maxAge;
}
