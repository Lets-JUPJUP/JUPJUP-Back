package efub.back.jupjup.domain.eventcomment.domain;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
public class Eventcomment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_comment_id", updatable = false)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", updatable = false)
	private Member writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_info_id", updatable = false)
	private EventInfo eventInfo;

	@Builder
	public Eventcomment(String content, Member writer, EventInfo eventInfo){
		this.content = content;
		this.writer = writer;
		this.eventInfo = eventInfo;
	}
}
