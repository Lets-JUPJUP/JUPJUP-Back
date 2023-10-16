package efub.back.jupjup.domain.post.domain;

import efub.back.jupjup.global.BaseTimeEntity;
import java.time.LocalDateTime;
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
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "age_range", nullable = false)
	private AgeRange ageRange;

	@Column(name = "due_date", nullable = false)
	private LocalDateTime dueDate;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Builder
	public Post(String title, String content, String startPlace,
		LocalDateTime startDate, int minMember, int maxMember, Gender gender, AgeRange ageRange, LocalDateTime dueDate,
		Long memberId) {
		this.title = title;
		this.content = content;
		this.startPlace = startPlace;
		this.startDate = startDate;
		this.minMember = minMember;
		this.maxMember = maxMember;
		this.gender = gender;
		this.ageRange = ageRange;
		this.dueDate = dueDate;
		this.memberId = memberId;
	}
}
