package efub.back.jupjup.domain.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReqDto {
	private String nickname;
	private String gender;
	private String ageRange;
	private String profileImage;
}
