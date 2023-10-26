package efub.back.jupjup.domain.auth.controller;

import efub.back.jupjup.domain.auth.dto.AccessTokenDto;
import efub.back.jupjup.domain.auth.service.AuthService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

@Tag(name = "인증.인가 API", description = "카카오 로그인을 통한 인증.인가 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/refresh")
    public ResponseEntity<StatusResponse> refresh(@CookieValue(value = "refreshToken", required = false) Cookie cookie){
        String refreshToken = cookie.getValue();
        return authService.refreshAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logout(@RequestBody AccessTokenDto accessTokenDto){
        authService.logout(accessTokenDto);
        ResponseCookie responseCookie = authService.removeRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(StatusResponse.builder()
                        .status(StatusEnum.OK.getStatusCode())
                        .message(StatusEnum.OK.getCode())
                        .data("로그아웃 성공")
                        .build());
    }

    @GetMapping("/test")
    public ResponseEntity<StatusResponse> test(@AuthUser Member member){
        String nickname = member.getNickname();
        return ResponseEntity.ok()
                .body(StatusResponse.builder()
                        .status(StatusEnum.OK.getStatusCode())
                        .message(StatusEnum.OK.getCode())
                        .data(nickname)
                        .build());
    }

}
