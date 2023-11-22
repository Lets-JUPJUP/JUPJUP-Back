package efub.back.jupjup.domain.eventjoin.domain;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.member.domain.Member;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eventjoin")
@Getter
public class Eventjoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "join_id")
	private Long Id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_info_id", nullable = false)
	private EventInfo eventInfo;

	@Column(name = "event_joined", nullable = false)
	private boolean eventJoined;

	@Builder
	public Eventjoin(Member member, EventInfo eventInfo, boolean eventJoined){
		this.member = member;
		this.eventInfo = eventInfo;
		this.eventJoined = eventJoined;
	}
}
