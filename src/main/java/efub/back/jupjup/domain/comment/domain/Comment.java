package efub.back.jupjup.domain.comment.domain;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id",updatable = false)
	private Long id;

	@Column(nullable = false)
	private String content;

	private boolean isRemoved;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", updatable = false)
	private Member writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", updatable = false)
	private Post post;

	// 댓글 삭제 (표시 삭제)
	public void remove() {
		this.isRemoved = true;
	}

	@Builder
	public Comment(String content, Member writer, Post post) {
		this.content = content;
		this.isRemoved = false;
		this.writer = writer;
		this.post = post;
	}
}
