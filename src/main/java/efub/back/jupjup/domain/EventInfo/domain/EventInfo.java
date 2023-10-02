package efub.back.jupjup.domain.EventInfo.domain;

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
	private Long eventinfoId;

	@Column(nullable = false)
	private String title;

	@Column(name = "info_url", nullable = false)
	private String info_url;

	@Column(name = "image_url", nullable = false)
	private String image_url;

	public EventInfo(String title, String info_url, String image_url){
		this.title = title;
		this.info_url = info_url;
		this.image_url = image_url;
	}
}
