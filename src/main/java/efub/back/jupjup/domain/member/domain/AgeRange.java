package efub.back.jupjup.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AgeRange {
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
    AGE_80_89("10", "80~89"),
    AGE_90_ABOVE("11", "90~");

    private final String code;
    private final String description;

    private static final Map<String, String> AGE_RANGE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(AgeRange::getDescription, AgeRange::name)));

    public static AgeRange fromString(final String ageRangeStr){
        AgeRange ageRange = AgeRange.valueOf(AGE_RANGE_MAP.get(ageRangeStr));
        if(ageRange.equals(AgeRange.AGE_10_14) || ageRange.equals(AgeRange.AGE_15_19)){
            ageRange = AgeRange.AGE_10_19;
        }

        return ageRange;
    }

    public int getStartAge() {
        switch (this) {
            case AGE_0_9: return 0;
            case AGE_10_14: return 10;
            case AGE_15_19: return 15;
            case AGE_10_19: return 10;
            case AGE_20_29: return 20;
            case AGE_30_39: return 30;
            case AGE_40_49: return 40;
            case AGE_50_59: return 50;
            case AGE_60_69: return 60;
            case AGE_70_79: return 70;
            case AGE_80_89: return 80;
            case AGE_90_ABOVE: return 90;
            default: throw new UnsupportedOperationException("Unsupported AgeRange: " + this);
        }
    }

    public int getEndAge() {
        switch (this) {
            case AGE_0_9: return 9;
            case AGE_10_14: return 14;
            case AGE_15_19: return 19;
            case AGE_10_19: return 19;
            case AGE_20_29: return 29;
            case AGE_30_39: return 39;
            case AGE_40_49: return 49;
            case AGE_50_59: return 59;
            case AGE_60_69: return 69;
            case AGE_70_79: return 79;
            case AGE_80_89: return 89;
            case AGE_90_ABOVE: return Integer.MAX_VALUE; // 90 이상의 경우 상한선이 없으므로 최대 정수값 사용
            default: throw new UnsupportedOperationException("Unsupported AgeRange: " + this);
        }
    }
}
