package efub.back.jupjup.domain.member.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import efub.back.jupjup.domain.admin.member.exception.AlreadyBlockedException;
import efub.back.jupjup.domain.member.exception.InvalidNicknameException;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import efub.back.jupjup.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
	public static final String INFO_UNKNOWN = "unknown";
	public static final String WITHDRAWN_NICKNAME = "(탈퇴한 회원)";

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

	@Column(name = "age", nullable = false)
	private Integer age;

	@Column(length = 50)
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
		ProviderType providerType, Integer age, RoleType roleType, MemberStatus status, AgeRange ageRange,
		Gender gender) {
		this.username = username;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.nickname = nickname;
		this.providerType = providerType;
		this.roleType = roleType;
		this.memberStatus = status;
		this.age = age;
		this.gender = gender;
	}

	public boolean validateNickName(String nickname) {
		if (Objects.isNull(nickname) || nickname.isBlank() || nickname.length() > 15 || !nickname.matches(
			"[ㄱ-ㅎ가-힣a-zA-Z0-9_]+") || nickname.equals(WITHDRAWN_NICKNAME)) {
			throw new InvalidNicknameException();
		} else {
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
		if (this.memberStatus.equals(MemberStatus.BLOCKED)) {
			throw new AlreadyBlockedException();
		}
		if (memberStatus != null && !this.memberStatus.equals(memberStatus)) {
			this.memberStatus = memberStatus;
		}
	}

	public void updateProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void updateAge(Integer age) {
		if (age != null && !this.age.equals(age)) {
			this.age = age;
		}
	}

	public void updateGender(Gender gender) {
		if (gender != null && !this.gender.equals(gender)) {
			this.gender = gender;
		}
	}

	public void withdrawInfoProcess() {
		this.nickname = WITHDRAWN_NICKNAME;
		this.email = INFO_UNKNOWN;
		this.username = INFO_UNKNOWN;
		this.providerType = null;
		this.profileImageUrl = null;
		this.memberStatus = MemberStatus.WITHDRAWN;
	}
}