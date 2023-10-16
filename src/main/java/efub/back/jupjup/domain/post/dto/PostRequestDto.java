package efub.back.jupjup.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import efub.back.jupjup.domain.post.domain.Gender;
import efub.back.jupjup.domain.post.domain.AgeRange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PostRequestDto {
	private String title;
	private String content;
	private String startPlace;
	private LocalDateTime startDate;
	private int minMember;
	private int maxMember;
	private Gender gender;
	private AgeRange ageRange;
	private LocalDateTime dueDate;
	private List<String> images;
}
