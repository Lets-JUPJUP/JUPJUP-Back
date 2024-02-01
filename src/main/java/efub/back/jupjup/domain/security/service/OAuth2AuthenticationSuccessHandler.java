package efub.back.jupjup.domain.security.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.security.exception.AuthExceptionHandler;
import efub.back.jupjup.domain.security.exception.BlockedAccountException;
import efub.back.jupjup.domain.security.repository.CookieAuthorizationRequestRepository;
import efub.back.jupjup.domain.security.userInfo.KakaoUserInfo;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import efub.back.jupjup.global.jwt.JwtProvider;
import efub.back.jupjup.global.redis.RedisService;
import efub.back.jupjup.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
	private final MemberRepository memberRepository;
	private final AuthExceptionHandler authExceptionHandler;

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.redirect-url}")
	private String adminRedirectUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken)authentication;
		ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());
		log.info(attributes.toString());

		String email = null;

		if (providerType.equals(ProviderType.KAKAO)) {
			KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
			email = kakaoUserInfo.getEmail();
			log.info("kakao");
		} else {
			Map<String, Object> providerData = (Map<String, Object>)attributes.get(providerType.name().toLowerCase());
			if (providerData != null) {
				email = providerData.get("email").toString();
			} else {
				// Handle the case where providerData is null
			}
		}

		// 활성 상태의 유저인지 확인
		if (!isUserActive(email)) {
			authExceptionHandler.handleException(response, new BlockedAccountException());
			return;
		}

		String targetUrl = determineTargetUrl(request, response, authentication);
		log.info("targetUrl = " + targetUrl);
		String oldUrl = request.getHeader("Referer");
		log.info("oldUrl :" + oldUrl);

		String url = makeRedirectUrl(email, targetUrl);

		ResponseCookie responseCookie = generateRefreshTokenCookie(email);
		response.setHeader("Set-Cookie", responseCookie.toString());
		response.getWriter().write(url);

		if (response.isCommitted()) {
			logger.info("응답이 이미 커밋된 상태입니다. " + url + "로 리다이렉트하도록 바꿀 수 없습니다.");
			return;
		}
		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, url);
	}

	private String makeRedirectUrl(String email, String redirectUrl) {

		redirectUrl = "https://lets-jupjup.com";
		if (email.equals(adminEmail)) {
			redirectUrl = adminRedirectUrl;
		}
		log.info(redirectUrl);

		String accessToken = jwtProvider.generateAccessToken(email);
		log.info(accessToken);

		return UriComponentsBuilder.fromHttpUrl(redirectUrl)
			.path("/kakao-login")
			.queryParam("accessToken", accessToken)
			.queryParam("redirectUrl", redirectUrl)
			.build()
			.encode()
			.toUriString();

	}

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		Optional<String> redirectUrl = CookieUtils.getCookie(request,
			CookieAuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_KEY).map(Cookie::getValue);
		String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());
		return UriComponentsBuilder.fromUriString(targetUrl)
			.build().toUriString();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

	public ResponseCookie generateRefreshTokenCookie(String email) {
		String refreshToken = jwtProvider.generateRefreshToken(email);
		Long refreshTokenValidationMs = jwtProvider.getRefreshTokenValidationMs();

		redisService.setData("RefreshToken:" + email, refreshToken, refreshTokenValidationMs);
		log.info("refresh:" + refreshToken);
		return ResponseCookie.from("refreshToken", refreshToken)
			.path("/") // 해당 경로 하위의 페이지에서만 쿠키 접근 허용. 모든 경로에서 접근 허용한다.
			.domain(".lets-jupjup.com")
			//                .domain(".lets-jupjup.com") //TODO : 배포시 도메인 변경
			.maxAge(TimeUnit.MILLISECONDS.toSeconds(refreshTokenValidationMs)) // 쿠키 만료 시기(초). 없으면 브라우저 닫힐 때 제거
			.secure(true) // HTTPS 프로토콜 상에서 암호화된 요청을 위한 옵션
			.sameSite("None") // Same Site 요청을 물론 Cross Site으 요청에도 모두 전송 허용
			.httpOnly(true) // XSS 공격을 방어하기 위한 옵션
			.build();
	}

	private boolean isUserActive(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow();
		if (member.getMemberStatus() != MemberStatus.ACTIVE) {
			return false;
		}
		return true;
	}
}
