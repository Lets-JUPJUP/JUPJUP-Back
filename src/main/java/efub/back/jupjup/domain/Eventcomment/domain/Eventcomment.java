package efub.back.jupjup.domain.Eventcomment.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Eventcomment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long eventcommentId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "anonymous", nullable = false)
	private Boolean anonymous;

	@Column(nullable = false)
	private String content;

	@Column(name = "eventinfo_id", nullable = false)
	private Long eventinfoId;

	public Eventcomment(Long memberId, Boolean anonymous, String content, Long eventinfoId){
		this.memberId = memberId;
		this.anonymous = anonymous;
		this.content = content;
		this.eventinfoId = eventinfoId;
	}
}
