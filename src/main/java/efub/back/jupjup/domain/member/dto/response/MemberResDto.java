package efub.back.jupjup.domain.member.dto.response;

import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResDto {
	private Long id;
	private String email;
	private String profileImageUrl;
	private String nickname;
	private ProviderType providerType;
	private Integer age;
	private Gender gender;

	public static MemberResDto from(Member member) {
		return MemberResDto.builder()
			.id(member.getId())
			.email(member.getEmail())
			.profileImageUrl(member.getProfileImageUrl())
			.nickname(member.getNickname())
			.providerType(member.getProviderType())
			.age(member.getAge())
			.gender(member.getGender())
			.build();
	}
}
