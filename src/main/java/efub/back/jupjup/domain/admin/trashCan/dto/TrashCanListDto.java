package efub.back.jupjup.domain.admin.trashCan.dto;

import java.util.List;

import efub.back.jupjup.global.response.PageInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanListDto {
	private PageInfo pageInfo;
	private List<TrashCanSimpleDto> trashCans;
}
