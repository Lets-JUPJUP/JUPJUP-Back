package efub.back.jupjup.global.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.security.exception.AuthExceptionHandler;
import efub.back.jupjup.domain.security.exception.BlockedAccountException;
import efub.back.jupjup.domain.security.exception.ExpiredTokenException;
import efub.back.jupjup.domain.security.exception.InvalidRefreshTokenException;
import efub.back.jupjup.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	private final RedisService redisService;
	private final MemberRepository memberRepository;
	private final AuthExceptionHandler authExceptionHandler;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String jwt = resolveToken(request);

		try {
			if (StringUtils.hasText(jwt)) {
				if (jwtProvider.validateToken(response, jwt)) {
					Optional<String> isBlackList = redisService.getBlackList(jwt);
					isBlackList.ifPresent(t -> {
						throw new InvalidRefreshTokenException();
					});
					Authentication authentication = jwtProvider.getAuthentication(jwt);

					// 관리자에 의해 탈퇴된 유저의 접근 차단
					isUserActive(authentication, response);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					return;
				}
			}
		} catch (InvalidRefreshTokenException e) {
			log.error("Invalid refresh token exception:", e);
			authExceptionHandler.handleException(response, new ExpiredTokenException());
			return;
		} catch (BlockedAccountException e) {
			authExceptionHandler.handleException(response, new BlockedAccountException());
			return;
		}

		filterChain.doFilter(request, response);
	}

	// 헤더에서 "Bearer " 뒤의 토큰만 추출
	public String resolveToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
			return authorization.substring(7);
		}
		return null;
	}

	private void isUserActive(Authentication authentication, HttpServletResponse response) throws
		BlockedAccountException {
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)principal;
			String email = userDetails.getUsername();
			Member member = memberRepository.findByEmail(email).orElseThrow();
			if (member.getMemberStatus() != MemberStatus.ACTIVE) {
				throw new BlockedAccountException();
			}
		}
	}
}