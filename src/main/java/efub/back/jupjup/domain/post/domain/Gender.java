package efub.back.jupjup.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
	ANY("any"),
	FEMALE("female"),
	MALE("male");

	private final String description;
}
