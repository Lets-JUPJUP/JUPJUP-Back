package efub.back.jupjup.domain.trashCan.domain;

import efub.back.jupjup.global.BaseTimeEntity;


import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BinFeedback extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private Feedback feedback;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trash_can_id", nullable = false)
	private TrashCan trashCan;

	@Builder
	public BinFeedback(Feedback feedback, TrashCan trashCan) {
		this.feedback = feedback;
		this.trashCan = trashCan;
	}
}
