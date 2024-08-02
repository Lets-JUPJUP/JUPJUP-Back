package efub.back.jupjup.domain.admin.trashCan.dto;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanSimpleDto {
	private Long trashCanId;
	private Long feedbackCount;
	private String address;
	private String detail;

	public static TrashCanSimpleDto from(TrashCan trashCan, Long feedbackCount) {
		return TrashCanSimpleDto.builder()
			.trashCanId(trashCan.getId())
			.feedbackCount(feedbackCount)
			.address(trashCan.getAddress())
			.build();
	}
}
