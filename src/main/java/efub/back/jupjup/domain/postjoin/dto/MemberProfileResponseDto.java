package efub.back.jupjup.domain.postjoin.dto;

import efub.back.jupjup.domain.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileResponseDto {
	private Long memberId;
	private String nickname;
	private String profileImageUrl;
	private Integer age;
	private Gender gender;
}
