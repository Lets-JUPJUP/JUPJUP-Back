package efub.back.jupjup.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {
	long memberId() default 1L;

	String email() default "gy5027@naver.com"; // TODO : 테스트용 이메일로 수정

	String role() default "MEMBER";
}

