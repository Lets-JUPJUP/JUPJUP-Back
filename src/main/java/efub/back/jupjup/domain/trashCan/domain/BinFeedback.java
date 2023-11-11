package efub.back.jupjup.domain.trashCan.domain;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.BaseTimeEntity;


import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = @Index(name = "idx_trash_can_id", columnList = "trash_can_id"))
public class BinFeedback extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private Feedback feedback;

	@Column(name = "trash_can_id")
	private Long trashCanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;


	@Builder
	public BinFeedback(Feedback feedback, Long trashCanId, Member member) {
		this.feedback = feedback;
		this.trashCanId = trashCanId;
		this.member = member;
	}
}
