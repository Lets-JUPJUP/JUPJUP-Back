package efub.back.jupjup.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    ACTIVE("활성된 계정"),
    WITHDRAWN("탈퇴된 계정"),
    BLOCKED("탈퇴된 계정");

    private final String description;
}
