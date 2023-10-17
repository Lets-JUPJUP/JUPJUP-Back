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
    AGE_80_80("10", "80~89"),
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
}
