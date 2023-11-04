package efub.back.jupjup.domain.postjoin.domain;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "postjoin")
@Getter
public class Postjoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_join_id")
	private Long Id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@Column(name = "is_joined", nullable = false)
	private boolean isJoined;

	@Builder
	public Postjoin(Member member, Post post, boolean isJoined){
		this.member = member;
		this.post = post;
		this.isJoined = isJoined;
	}
}
