package efub.back.jupjup.domain.post.domain;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostAgeRange {
	AGE_0_9("0", "0~9"),
	AGE_10_14("1", "10~14"),
	AGE_15_19("2", "15~19"),
	AGE_10_19("3", "10~19"),
	AGE_20_29("4", "20~29"),
	AGE_30_39("5", "30~39"),
	AGE_40_49("6", "40~49"),
	AGE_50_59("7", "50~59"),
	AGE_60_69("8", "60~69"),
	AGE_70_79("9", "70~79"),
	AGE_80_80("10", "80~89"),
	AGE_90_ABOVE("11", "90~"),
	AGE_ANY("12", "any");

	private final String code;
	private final String description;

	private static final Map<String, String> AGE_RANGE_MAP = Collections.unmodifiableMap(
		Stream.of(values()).collect(Collectors.toMap(PostAgeRange::getDescription, PostAgeRange::name)));

	public static PostAgeRange fromString(final String ageRangeStr){
		PostAgeRange ageRange = PostAgeRange.valueOf(AGE_RANGE_MAP.get(ageRangeStr));
		if(ageRange.equals(PostAgeRange.AGE_10_14) || ageRange.equals(PostAgeRange.AGE_15_19)){
			ageRange = PostAgeRange.AGE_10_19;
		}

		return ageRange;
	}
}
