package efub.back.jupjup.domain.post.domain;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import efub.back.jupjup.domain.member.domain.AgeRange;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostAgeRange {
	AGE_0_9("0", "0~9"),
	AGE_10_19("1", "10~19"),
	AGE_20_29("2", "20~29"),
	AGE_30_39("3", "30~39"),
	AGE_40_49("4", "40~49"),
	AGE_50_59("5", "50~59"),
	AGE_60_69("6", "60~69"),
	AGE_70_79("7", "70~79"),
	AGE_80_89("8", "80~89"),
	AGE_90_ABOVE("9", "90~"),
	AGE_ANY("10", "any");

	private final String code;
	private final String description;

	private static final Map<String, String> AGE_RANGE_MAP = Collections.unmodifiableMap(
		Stream.of(values()).collect(Collectors.toMap(PostAgeRange::getDescription, PostAgeRange::name)));

	public static PostAgeRange fromString(final String ageRangeStr) {
		PostAgeRange ageRange = PostAgeRange.valueOf(AGE_RANGE_MAP.get(ageRangeStr));

		return ageRange;
	}

	// 멤버의 나이 범위가 포스트의 나이 범위에 포함되는지 확인
	public boolean includes(AgeRange memberAgeRange) {
		// 모든 범위를 허용하는 경우 항상 true 반환
		if (this == AGE_ANY) {
			return true;
		}

		int startAge = memberAgeRange.getStartAge();
		int endAge = memberAgeRange.getEndAge();

		switch (this) {
			case AGE_0_9:
				return startAge >= 0 && endAge <= 9;
			case AGE_10_19:
				return startAge >= 10 && endAge <= 19;
			case AGE_20_29:
				return startAge >= 20 && endAge <= 29;
			case AGE_30_39:
				return startAge >= 30 && endAge <= 39;
			case AGE_40_49:
				return startAge >= 40 && endAge <= 49;
			case AGE_50_59:
				return startAge >= 50 && endAge <= 59;
			case AGE_60_69:
				return startAge >= 60 && endAge <= 69;
			case AGE_70_79:
				return startAge >= 70 && endAge <= 79;
			case AGE_80_89:
				return startAge >= 80 && endAge <= 89;
			case AGE_90_ABOVE:
				return startAge >= 90;
			case AGE_ANY:
				return true; // 모든 나이 범위를 허용
			default:
				return false; // 정의되지 않은 범위
		}
	}
}

