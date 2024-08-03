package efub.back.jupjup.domain.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.BaseTimeEntity;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", updatable = false)
	private Long Id;

	@Column(nullable = false)
	private String title;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "due_date", nullable = false)
	private LocalDateTime dueDate;

	@Column(name = "min_age", nullable = false)
	private int minAge;

	@Column(name = "max_age", nullable = false)
	private int maxAge;

	@Column(name = "min_member", nullable = false)
	private int minMember;

	@Column(name = "max_member", nullable = false)
	private int maxMember;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_gender", nullable = false)
	private PostGender postGender;

	@Column(name = "with_pet", nullable = false)
	private boolean withPet;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "district", nullable = false)
	private District district;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "post_id")
	private List<Route> route = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member author;

	@Column(name = "is_recruitment_successful", nullable = false)
	private Boolean isRecruitmentSuccessful;

	public void updateIsRecruitmentSuccessful(Long memberCount) {
		if (memberCount < minMember) {
			this.isRecruitmentSuccessful = false;
			return;
		}
		this.isRecruitmentSuccessful = true;
	}

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> commentList = new ArrayList<>();

	@Builder
	public Post(String title, LocalDateTime startDate, LocalDateTime dueDate, int minAge, int maxAge, int minMember, int maxMember,
		PostGender postGender, boolean withPet, String content, District district, List<Route> route, Member author, Boolean isRecruitmentSuccessful) {
		this.title = title;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.minMember = minMember;
		this.maxMember = maxMember;
		this.postGender = postGender;
		this.withPet = withPet;
		this.content = content;
		this.district = district;
		this.route = route;
		this.author = author;
		this.isRecruitmentSuccessful = isRecruitmentSuccessful;
	}
}
