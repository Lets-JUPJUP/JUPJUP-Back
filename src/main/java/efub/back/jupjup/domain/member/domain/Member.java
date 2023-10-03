package efub.back.jupjup.domain.member.domain;

import efub.back.jupjup.global.BaseTimeEntity;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import efub.back.jupjup.domain.member.exception.InvalidNicknameException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
    public static final String FORBIDDEN_WORD = "unknown";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT", name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public Member(String username, String email, String profileImageUrl, String nickname,
                  ProviderType providerType, RoleType roleType, MemberStatus status, AgeRange ageRange, Gender gender) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.providerType = providerType;
        this.roleType = roleType;
        this.memberStatus = status;
        this.ageRange = ageRange;
        this.gender = gender;
    }

    public boolean validateNickName(String nickname) {
        if (Objects.isNull(nickname) || nickname.isBlank() || nickname.length() > 15 || !nickname.matches("[a-zA-Z0-9_]+") || nickname.equalsIgnoreCase(FORBIDDEN_WORD)) {
            throw new InvalidNicknameException();
        }else{
            return true;
        }
    }
    public void updateMember(String email) {
        if (!this.email.equals(email)) {
            this.email = email;
        }
    }
    public void updateNickname(String nickname) {
        if (validateNickName(nickname)) {
            this.nickname = nickname;
        }
    }
    public void updateRole(RoleType roleType) {
        if (roleType != null && !this.roleType.equals(roleType)) {
            this.roleType = roleType;
        }
    }
    public void updateMemberStatus(MemberStatus memberStatus) {
        if (memberStatus != null && !this.memberStatus.equals(memberStatus)) {
            this.memberStatus = memberStatus;
        }
    }
    public void updateProfileImage(String profileImageUrl){
        if(!this.profileImageUrl.equals(profileImageUrl)){
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void updateAgeRange(AgeRange ageRange) {
        if (ageRange != null && !this.ageRange.equals(ageRange)) {
            this.ageRange = ageRange;
        }
    }

    public void updateGender(Gender gender) {
        if (gender != null && !this.gender.equals(gender)) {
            this.gender = gender;
        }
    }
    public void withdrawInfoProcess(){
        this.nickname = FORBIDDEN_WORD;
        this.email = FORBIDDEN_WORD;
        this.username = FORBIDDEN_WORD;
        this.providerType = null;
        this.profileImageUrl = null;
        this.memberStatus = MemberStatus.WITHDRAWN;
    }
}