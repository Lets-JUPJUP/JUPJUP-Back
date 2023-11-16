package efub.back.jupjup.domain.trashCan.dto.response;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TrashCansResDto {
    private Double radius;
    private List<TrashCan> trashCans;


    public TrashCansResDto(Double radius, List<TrashCan> trashCans) {
        this.radius = radius;
        this.trashCans = trashCans;
    }
}
