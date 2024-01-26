package efub.back.jupjup.domain.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import efub.back.jupjup.domain.auth.controller.AuthController;
import efub.back.jupjup.domain.auth.service.AuthService;
import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.domain.RoleType;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.member.service.MemberService;
import efub.back.jupjup.domain.security.exception.AuthExceptionHandler;
import efub.back.jupjup.domain.security.userInfo.ProviderType;
import efub.back.jupjup.global.jwt.JwtProvider;
import efub.back.jupjup.global.redis.RedisService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {
	AuthController.class,
	MemberController.class //TODO : 클래스 추가
})
@MockBean(JpaMetamodelMappingContext.class)
public abstract class PreProcessController {
	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected MemberRepository memberRepository;
	@MockBean
	protected JwtProvider jwtProvider;
	@MockBean
	protected AuthService authService;
	@MockBean
	protected MemberService memberService;
	@MockBean
	protected RedisService redisService;
	@MockBean
	protected AuthExceptionHandler authExceptionHandler;

	protected ObjectMapper objectMapper = new ObjectMapper();
	protected Member loginMember;
	protected Long loginMemberId = 1L;
	protected String loginEmail;

	protected void loginMockSetUp() {
		loginMember = Member.testerBuilder()
			.id(loginMemberId)
			.nickname("nickname")
			.ageRange(AgeRange.AGE_20_29)
			.email(loginEmail)
			.gender(Gender.FEMALE)
			.memberStatus(MemberStatus.ACTIVE)
			.providerType(ProviderType.KAKAO)
			.roleType(RoleType.MEMBER)
			.build();

		willReturn(true).given(jwtProvider).validateToken(anyString());
		willReturn(loginEmail).given(jwtProvider).tokenToEmail(anyString());
		willReturn(Optional.of(loginMember)).given(memberRepository).findByEmail(anyString());
	}
}


