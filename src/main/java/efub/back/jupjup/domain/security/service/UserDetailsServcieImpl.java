package efub.back.jupjup.domain.security.service;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberAdapter;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServcieImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        // UserDetails를 반환한다.
        return new MemberAdapter(member);
    }

}
