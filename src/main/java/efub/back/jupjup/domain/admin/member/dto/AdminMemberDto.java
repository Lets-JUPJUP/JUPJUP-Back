package efub.back.jupjup.domain.admin.member.dto;

import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminMemberDto {
	private Long id;
	private String nickname;
	private String email;
	private Integer age;
	private Gender gender;
	private MemberStatus memberStatus;

	public static AdminMemberDto from(Member member) {
		return AdminMemberDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.email(member.getEmail())
			.age(member.getAge())
			.gender(member.getGender())
			.memberStatus(member.getMemberStatus())
			.build();
	}
}
