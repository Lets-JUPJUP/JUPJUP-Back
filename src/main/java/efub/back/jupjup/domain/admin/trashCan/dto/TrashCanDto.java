package efub.back.jupjup.domain.admin.trashCan.dto;

import java.math.BigDecimal;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanDto {
	private Long trashCanId;
	private String address;
	private String detail;
	private BigDecimal latitude;
	private BigDecimal longitude;

	public static TrashCanDto from(TrashCan trashCan) {
		return TrashCanDto.builder()
			.trashCanId(trashCan.getId())
			.address(trashCan.getAddress())
			.latitude(trashCan.getLatitude())
			.longitude(trashCan.getLongitude())
			.build();
	}
}
