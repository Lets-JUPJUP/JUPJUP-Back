package efub.back.jupjup.domain.postjoin.domain;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "postjoin")
@Getter
public class Postjoin extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long joinedId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public Postjoin(Long memberId, Long postId){
		this.memberId = memberId;
		this.postId = postId;
	}
}
