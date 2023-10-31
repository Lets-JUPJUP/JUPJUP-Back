package efub.back.jupjup.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostGender {
	FEMALE("female"),
	MALE("male"),
	NOT_DEFINED("not defined");

	private final String description;
}
