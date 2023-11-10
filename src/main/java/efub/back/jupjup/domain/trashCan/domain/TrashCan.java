package efub.back.jupjup.domain.trashCan.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TrashCan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_can_id")
    private Long id;

    private String address;
    private String detail;

    @Column(precision = 18, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 18, scale = 10)
    private BigDecimal longitude;

    @Column(length = 50, name = "trash_can_type")
    @Enumerated(EnumType.STRING)
    private TrashCanType trashCanType;

    @Column(length = 50, name = "trash_category")
    private String trashCategory;

}
