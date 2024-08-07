package efub.back.jupjup.domain.post.dto;

import java.util.List;

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
	private List<District> districts;
	private Boolean allAge; // '연령무관' 옵션
	private Boolean allGender; // '성별무관' 옵션
	private Boolean excludeClosedRecruitment;
	private Gender userGender;
	private Integer userAge;
}
