package efub.back.jupjup.domain.security.service;

import static efub.back.jupjup.domain.member.domain.Member.*;

import java.util.Objects;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.domain.RoleType;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.security.userInfo.KakaoUserInfo;
import efub.back.jupjup.domain.security.userInfo.OAuth2UserInfo;
import efub.back.jupjup.domain.security.userInfo.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("getAttributes : {}", oAuth2User.getAttributes());

		OAuth2UserInfo oAuth2UserInfo = null;
		String provider = userRequest.getClientRegistration().getRegistrationId();

		if (provider.equals("kakao")) {
			log.info("카카오 로그인");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		}

		Member member = saveOrUpdate(oAuth2UserInfo);
		return new PrincipalDetails(member, oAuth2User.getAttributes());
	}

	private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
		Member member = memberRepository.findByUsername(oAuth2UserInfo.getName())
			.map(entity -> {
				entity.updateMember(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getAgeRange());
				return entity;
			})
			.orElseGet(() -> {
				if (!memberRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
					String nickname = oAuth2UserInfo.getNickname();
					log.info("카카오 닉네임 : " + nickname);
					if (invalidateNickname(nickname)) {
						nickname = String.valueOf(System.currentTimeMillis());
					}
					Gender gender = null;
					AgeRange ageRange = null;
					
					if (!oAuth2UserInfo.getAttributes().containsKey("gender")) {
						gender = Gender.NOT_DEFINED;
					} else {
						gender = Gender.valueOf(oAuth2UserInfo.getGender().toUpperCase().trim());
					}

					if (!oAuth2UserInfo.getAttributes().containsKey("age_range")) {
						ageRange = AgeRange.NOT_DEFINED;
					} else {
						ageRange = AgeRange.fromString(oAuth2UserInfo.getAgeRange());
					}

					return Member.builder()
						.nickname(nickname)
						.email(oAuth2UserInfo.getEmail())
						.profileImageUrl(oAuth2UserInfo.getProfileImageUrl())
						.username(oAuth2UserInfo.getName())
						.providerType(oAuth2UserInfo.getProvider())
						.ageRange(ageRange)
						.gender(gender)
						.roleType(RoleType.MEMBER)
						.status(MemberStatus.ACTIVE)
						.build();
				} else {
					return null;
				}
			});

		if (member == null) {
			member = memberRepository.findByEmail(oAuth2UserInfo.getEmail()).orElseThrow(MemberNotFoundException::new);
			member.updateMember(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getAgeRange());
			return member;
		}

		return memberRepository.save(member);
	}

	public boolean invalidateNickname(String nickname) {
		if (Objects.isNull(nickname) || nickname.isBlank() || nickname.length() > 15 || !nickname.matches(
			"[ㄱ-ㅎ가-힣a-zA-Z0-9_]+") || nickname.equalsIgnoreCase(INFO_UNKNOWN)) {
			return true;
		}

		if (memberRepository.existsByNickname(nickname)) {
			return true;
		}
		return false;
	}
}
