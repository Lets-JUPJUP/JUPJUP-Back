package efub.back.jupjup.domain.post.dto;

import efub.back.jupjup.domain.member.domain.Gender;
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
	private Boolean excludeClosedRecruitment;
	private Gender userGender;
	private Boolean includeAllGenders; // '성별무관' 옵션
	private Boolean includeUserGender; // '내 성별 포함' 옵션
}
