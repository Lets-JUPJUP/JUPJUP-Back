package efub.back.jupjup.domain.admin.member.dto;

import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminMemberDto {
	private Long id;
	private String nickname;
	private String email;
	private AgeRange ageRange;
	private Gender gender;

	public static AdminMemberDto from(Member member) {
		return AdminMemberDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.email(member.getEmail())
			.ageRange(member.getAgeRange())
			.gender(member.getGender())
			.build();
	}
}
