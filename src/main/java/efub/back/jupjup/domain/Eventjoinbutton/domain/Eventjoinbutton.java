package efub.back.jupjup.domain.Eventjoinbutton.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Eventjoinbutton extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long EventjoinbuttonId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "eventinfo_id", nullable = false)
	private Long eventinfoId;

	public Eventjoinbutton(Long memberId, Long eventinfoId){
		this.memberId = memberId;
		this.eventinfoId = eventinfoId;
	}
}
