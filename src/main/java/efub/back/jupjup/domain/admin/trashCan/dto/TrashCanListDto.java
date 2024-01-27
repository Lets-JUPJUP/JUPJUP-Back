package efub.back.jupjup.domain.admin.trashCan.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanListDto {
	private int trashCanCount;
	private List<TrashCanSimpleDto> trashCans;
}
