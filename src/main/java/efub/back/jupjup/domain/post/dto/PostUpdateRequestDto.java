package efub.back.jupjup.domain.post.dto;

import efub.back.jupjup.domain.post.domain.Gender;
import efub.back.jupjup.domain.post.domain.AgeRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {
	private String title;
	private String content;
	private String startPlace;
	private LocalDateTime startDate;
	private int minMember;
	private int maxMember;
	private Gender gender;
	private AgeRange ageRange;
	private LocalDateTime dueDate;
	// Image 관련 필드 추가해야함
}
