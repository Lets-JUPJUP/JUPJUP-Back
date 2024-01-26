package efub.back.jupjup.domain.auth.service;

import javax.transaction.Transactional;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.auth.dto.AccessTokenDto;
import efub.back.jupjup.domain.auth.exception.AlreadyLogoutException;
import efub.back.jupjup.domain.auth.exception.RefreshTokenNotValidException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.security.exception.InvalidRefreshTokenException;
import efub.back.jupjup.global.jwt.JwtProvider;
import efub.back.jupjup.global.redis.RedisService;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final MemberRepository memberRepository;

	public void logout(AccessTokenDto accessTokenDto) {
		String accessToken = accessTokenDto.getAccessToken();
		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		// 이미 로그아웃 된 사용자는 예외 처리
		String refreshToken = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);

		// accessToken과 동일한 만료시간으로 블랙리스트 설정 후 리프레시 토큰은 redis에서 삭제
		Long remainingTime = jwtProvider.getRemainingTime(accessToken);
		redisService.setData("BlackList:" + accessToken, "logout", remainingTime);
		redisService.deleteData("RefreshToken:" + authentication.getName());
	}

	public ResponseCookie removeRefreshTokenCookie() {
		return ResponseCookie.from("refreshToken", null).path("/")
			.domain(".lets-jupjup.com")
			.maxAge(0) // 쿠키의 expiration 타임을 0으로 하여 없앤다.
			.secure(true) // HTTPS로 통신할 때만 쿠키가 전송된다.
			.httpOnly(true) // JS를 통한 쿠키 접근을 막아, XSS 공격 등을 방어하기 위한 옵션이다.
			.build();
	}

	public void withdrawInRedis(Authentication authentication, String accessToken) {
		String refreshToken = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);

		// redis 블랙리스트 등록
		Long remainingTime = jwtProvider.getRemainingTime(accessToken);
		redisService.setData("BlackList:" + accessToken, "signout", remainingTime);
		redisService.deleteData("RefreshToken:" + authentication.getName());

	}

	public void blockInRedis(String email) {
		// redis 리프레시 토큰 삭제 (발행된 액세스 토큰을 이용한 블랙리스트 추가는 불가능)
		redisService.deleteData("RefreshToken:" + email);
	}

	public ResponseEntity<StatusResponse> refreshAccessToken(String refreshToken) {
		Boolean isValidRefreshToken = jwtProvider.validateToken(refreshToken);
		if (!isValidRefreshToken) {
			throw new InvalidRefreshTokenException();
		}

		Authentication authentication = jwtProvider.getAuthentication(refreshToken);
		String refreshTokenRedis = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);
		if (!refreshToken.equals(refreshTokenRedis)) {
			throw new RefreshTokenNotValidException();
		}

		String newAccessToken = jwtProvider.generateAccessToken(authentication.getName());
		AccessTokenDto newAccessTokenDto = new AccessTokenDto(newAccessToken);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(newAccessTokenDto)
			.build());
	}
}
