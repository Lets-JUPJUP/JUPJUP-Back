package efub.back.jupjup.domain.member.dto.response;

import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyProfileResDto {
    private Long id;
    private String email;
    private String profileImageUrl;
    private String nickname;
    private ProviderType providerType;
    private AgeRange ageRange;
    private Gender gender;
    private Boolean isProfileCreated;

    public static MyProfileResDto from(Member member){
        return MyProfileResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImageUrl())
                .nickname(member.getNickname())
                .providerType(member.getProviderType())
                .ageRange(member.getAgeRange())
                .gender(member.getGender())
                .isProfileCreated(member.getModifiedAt() != null)
                .build();
    }
}
