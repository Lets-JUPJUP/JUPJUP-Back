package efub.back.jupjup.domain.binfeedback.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Binfeedback extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long binfeedbackId;

	@Enumerated(EnumType.STRING)
	private FeedbackType feedbackType;

	public enum FeedbackType {
		CLEAN, // 깨끗해요
		NEEDS_MAINTENANCE, // 관리가 필요해요
		MISSING_PLACE// 위치가 정확하지 않아요
	}

	public Binfeedback(FeedbackType feedbackType){
		this.feedbackType = feedbackType;
	}
}
