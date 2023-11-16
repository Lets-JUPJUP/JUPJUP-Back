package efub.back.jupjup.domain.trashCan.dto;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Location {
    private Double latitude;
    private Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}