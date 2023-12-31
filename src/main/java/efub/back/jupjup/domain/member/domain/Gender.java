package efub.back.jupjup.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("female"),
    MALE("male"),
    NOT_DEFINED("not defined");

    private final String description;
}
