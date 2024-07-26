package efub.back.jupjup.domain.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReqDto {
	private String nickname;
	private String gender;
	private Integer age;
	private String profileImage;
}
