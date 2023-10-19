package efub.back.jupjup.domain.post.domain;

import efub.back.jupjup.global.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long postId;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "start_place", nullable = false)
	private String startPlace;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "min_member", nullable = false)
	private int minMember;

	@Column(name = "max_member", nullable = false)
	private int maxMember;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PostGender postGender;

	@ElementCollection(targetClass = PostAgeRange.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "post_age_range", joinColumns = @JoinColumn(name = "post_id"))
	@Enumerated(EnumType.STRING)
	private List<PostAgeRange> postAgeRanges = new ArrayList<>();


	@Column(name = "due_date", nullable = false)
	private LocalDateTime dueDate;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Builder
	public Post(String title, String content, String startPlace,
		LocalDateTime startDate, int minMember, int maxMember, PostGender postGender, List<PostAgeRange> postAgeRanges, LocalDateTime dueDate,
		Long memberId) {
		this.title = title;
		this.content = content;
		this.startPlace = startPlace;
		this.startDate = startDate;
		this.minMember = minMember;
		this.maxMember = maxMember;
		this.postGender = postGender;
		this.postAgeRanges = postAgeRanges;
		this.dueDate = dueDate;
		this.memberId = memberId;
	}
}
