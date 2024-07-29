package efub.back.jupjup.domain.score.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Badge {
	SPEED("0", "걸음이 빠른 스피드 플로거"),
	FIRE("1", "열정이 넘치는 파이어 플로거"),
	SLOW("2", "여유로운 슬로우 플로거"),
	KIND("3", "친절한 성동 가이드 금자씨"),
	PIONEER("4", "가본 적 없는 루트 개척자"),
	ECO("5", "환경을 사랑하는 에코 플로거"),
	PUNCTUAL("6", "칼같은 시간 관리의 달인"),
	MAESTRO("7", "성동구 거리를 지배하는 지리 마에스트로"),
	NEAT("8", "깔끔함과 꼼꼼함의 대명사"),
	HAPPY("9", "긍정적인 해피 바이러스"),
	MASTER("10", "마무리도 완벽한 플로깅 고수"),
	PARTNER("11", "함께 또 걷고 싶은 플로깅 파트너"),
	PET("12", "귀엽고 깜찍한 네 발 친구");

	private final String code;
	private final String title;

	public static Badge getBadgeByCode(String code) {
		for (Badge badge : Badge.values()) {
			if (badge.getCode().equals(code)) {
				return badge;
			}
		}
		return null;
	}

	public static Badge getBadgeByTitle(String title) {
		for (Badge badge : Badge.values()) {
			if (badge.getTitle().equals(title)) {
				return badge;
			}
		}
		throw new IllegalArgumentException("No Badge found for this title: " + title);
	}

	public static boolean isCodeExists(String code) {
		for (Badge badge : Badge.values()) {
			if (badge.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
