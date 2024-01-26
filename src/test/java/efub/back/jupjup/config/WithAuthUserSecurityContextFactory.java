package efub.back.jupjup.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberAdapter;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.domain.RoleType;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
	@Override
	public SecurityContext createSecurityContext(WithAuthUser annotation) {
		String email = annotation.email();
		String role = annotation.role();

		Member member = Member.builder()
			.nickname("tester")
			.email(email)
			.roleType(RoleType.fromString(role))
			.status(MemberStatus.ACTIVE)
			.gender(Gender.FEMALE)
			.ageRange(AgeRange.AGE_20_29)
			.username("username")
			.build();
		UserDetails userDetails = new MemberAdapter(member);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
		return context;
	}
}

