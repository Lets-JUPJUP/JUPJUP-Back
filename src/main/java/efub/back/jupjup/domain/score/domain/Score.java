package efub.back.jupjup.domain.score.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Score extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "score_id", updatable = false)
	private Long id;

	@NotNull
	@Range(min = 1, max = 5)
	private Integer score;

	@ManyToOne
	private Member participant;

	@ManyToOne
	private Post post;

	@Builder
	public Score(Integer score, Member participant, Post post) {
		this.score = score;
		this.participant = participant;
		this.post = post;
	}
}
