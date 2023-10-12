package efub.back.jupjup.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeRange {
	AGE_0_9("0", "0~9세"),
	AGE_10_14("1", "10~14세"),
	AGE_15_19("2", "15~19세"),
	AGE_20_29("3", "20~29세"),
	AGE_30_39("4", "30~39세"),
	AGE_40_49("5", "40~49세"),
	AGE_50_59("6", "50~59세"),
	AGE_60_69("7", "60~69세"),
	AGE_70_79("8", "70~79세"),
	AGE_80_80("9", "80~89세"),
	AGE_90_ABOVE("10", "90세~");

	private final String code;
	private final String description;

}
