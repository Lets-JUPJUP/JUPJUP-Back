package efub.back.jupjup.domain.admin.trashCan.dto;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.domain.TrashCanType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanSimpleDto {
	private Long trashCanId;
	private Long feedbackCount;
	private String address;
	private String detail;
	private TrashCanType trashCanType;
	private String trashCategory;

	public static TrashCanSimpleDto from(TrashCan trashCan, Long feedbackCount) {
		return TrashCanSimpleDto.builder()
			.trashCanId(trashCan.getId())
			.feedbackCount(feedbackCount)
			.address(trashCan.getAddress())
			.detail(trashCan.getDetail())
			.trashCanType(trashCan.getTrashCanType())
			.trashCategory(trashCan.getTrashCategory())
			.build();
	}
}
