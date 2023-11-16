package efub.back.jupjup.domain.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.BaseTimeEntity;
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
	@Column(name = "post_id", updatable = false)
	private Long Id;

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
	@Column(name = "post_gender", nullable = false)
	private PostGender postGender;

	@ElementCollection(targetClass = PostAgeRange.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "post_age_range", joinColumns = @JoinColumn(name = "post_id"))
	@Enumerated(EnumType.STRING)
	private List<PostAgeRange> postAgeRanges = new ArrayList<>();

	@Column(name = "due_date", nullable = false)
	private LocalDateTime dueDate;

	@Column(name = "with_pet", nullable = false)
	private boolean withPet;

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

	@Builder
	public Post(String title, String content, String startPlace,
		LocalDateTime startDate, int minMember, int maxMember, PostGender postGender, List<PostAgeRange> postAgeRanges,
		LocalDateTime dueDate,
		boolean withPet, Member author, Boolean isRecruitmentSuccessful) {
		this.title = title;
		this.content = content;
		this.startPlace = startPlace;
		this.startDate = startDate;
		this.minMember = minMember;
		this.maxMember = maxMember;
		this.postGender = postGender;
		this.postAgeRanges = postAgeRanges;
		this.dueDate = dueDate;
		this.withPet = withPet;
		this.author = author;
		this.isRecruitmentSuccessful = isRecruitmentSuccessful;
	}
}
