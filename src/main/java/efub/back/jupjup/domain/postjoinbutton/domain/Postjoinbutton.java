package efub.back.jupjup.domain.postjoinbutton.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Postjoinbutton extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long PostjoinbuttonId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public Postjoinbutton(Long memberId, Long postId){
		this.memberId = memberId;
		this.postId = postId;
	}
}
