package efub.back.jupjup.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    MEMBER("MEMBER"),
    ADMIN("ADMIN"),
    GUEST("GUEST");

    private final String role;

    public static RoleType fromString(String role ) {
        for(RoleType r : RoleType.values()){
            if(r.role.equals(role)){
                return r;
            }
        }
        return null;
    }
}
