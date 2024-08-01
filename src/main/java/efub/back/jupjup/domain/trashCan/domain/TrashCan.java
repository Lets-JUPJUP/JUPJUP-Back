package efub.back.jupjup.domain.trashCan.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TrashCan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trash_can_id")
	private Long id;

	private String address; // 도로명 주소 + 세부 위치
	private String name; // {자치구명} + {수거쓰레기종류} + 통

	@Column(precision = 18, scale = 10)
	private BigDecimal latitude;

	@Column(precision = 18, scale = 10)
	private BigDecimal longitude;

	@Column(columnDefinition = "POINT")
	@JsonIgnore
	private Point location;
}
