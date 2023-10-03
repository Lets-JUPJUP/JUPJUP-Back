package efub.back.jupjup.domain.member.exception;

import efub.back.jupjup.global.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String memberEmail) {
        super("member email : " + memberEmail);
    }
}
