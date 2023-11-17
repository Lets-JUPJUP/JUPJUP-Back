package efub.back.jupjup.domain.eventInfo.domain;

import efub.back.jupjup.global.BaseTimeEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class EventInfo extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventinfo_id", updatable = false)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(name = "info_url", nullable = false)
	private String infoUrl;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	public EventInfo(String title, String infoUrl, String imageUrl){
		this.title = title;
		this.infoUrl = infoUrl;
		this.imageUrl = imageUrl;
	}
}
