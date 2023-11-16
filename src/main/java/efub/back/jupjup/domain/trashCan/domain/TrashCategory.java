package efub.back.jupjup.domain.trashCan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrashCategory {
    GENERAL("일반쓰레기"),
    GENERAL_TABACO("일반+담배꽁초"),
    RECYCLABLE("재활용쓰레기 수거용"),
    TABACO("담배꽁초 수거용"),
    TABACO_DRINK("담배꽁초, 음료컵"),
    NONE(null);

    private final String description;
}
