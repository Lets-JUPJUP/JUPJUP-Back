package efub.back.jupjup.domain.trashCan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrashCanType {
    STREET_TRASH_CAN("가로 쓰레기통"),
    RECYCLING_STATION("재활용 정거장");

    private final String description;
}
