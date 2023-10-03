package efub.back.jupjup.global.jwt;

import efub.back.jupjup.global.redis.RedisService;
import efub.back.jupjup.domain.security.exception.AuthExceptionHandler;
import efub.back.jupjup.domain.security.exception.InvalidRefreshTokenException;
import efub.back.jupjup.domain.security.exception.ExpiredTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final RedisService redisService;
    private final AuthExceptionHandler authExceptionHandler;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = resolveToken(request);

        try {
            if (StringUtils.hasText(jwt)) {
                if (jwtProvider.validateToken(response, jwt)) {
                    Authentication authentication = jwtProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    return;
                }
            }
        } catch (InvalidRefreshTokenException e) {
            log.error("Invalid refresh token exception:", e);
            authExceptionHandler.handleException(response, new ExpiredTokenException());
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
}