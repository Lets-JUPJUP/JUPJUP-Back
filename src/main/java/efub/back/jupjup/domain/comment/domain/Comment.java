package efub.back.jupjup.domain.comment.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long commentId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Boolean anonymous;

	@Column(nullable = false)
	private String content;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public Comment(Long memberId, Boolean anonymous, String content, Long postId){
		this.memberId = memberId;
		this.anonymous = anonymous;
		this.content = content;
		this.postId = postId;
	}
}
