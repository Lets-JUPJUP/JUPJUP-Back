package efub.back.jupjup.domain.comment.domain;

import java.util.ArrayList;
import java.util.List;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent;

	// 대댓글 나타내는 필드
	@OneToMany(mappedBy = "parent")
	private List<Comment> childList = new ArrayList<>();

	// 댓글 삭제 (표시 삭제)
	public void remove() {
		this.isRemoved = true;
	}

	// 대댓글 추가
	public void addChild(Comment child) {
		childList.add(child);
	}

	// 대댓글에 부모 댓글 추가
	public void setParent(Comment parent) {
		this.parent = parent;
		parent.addChild(this);
	}

	@Builder
	public Comment(String content, Member writer, Post post) {
		this.content = content;
		this.isRemoved = false;
		this.writer = writer;
		this.post = post;
	}

	public List<Comment> findCommentListToDelete() {
		List<Comment> result = new ArrayList<>();
		if (this.parent != null) { // 대댓글인 경우
			processReply(result);
		} else { // 일반 댓글인 경우
			processComment(result);
		}

		return result;
	}

	// 대댓글 처리
	private void processReply(List<Comment> result) {
		Comment parentComment = this.parent;

		if (parentComment.isRemoved() && parentComment.isAllChildRemoved()) {
			result.addAll(parentComment.getChildList());
			result.add(parentComment);
		}
	}

	// 일반 댓글 처리
	private void processComment(List<Comment> result) {
		if (isAllChildRemoved()) {
			result.add(this);
			result.addAll(this.getChildList());
		}
	}

	// 모든 자식들이 삭제 상태인지 확인하기
	private boolean isAllChildRemoved() {
		if (childList == null) {
			return true;
		}
		return getChildList().stream()
			.allMatch(comment -> comment.isRemoved);
	}
}
