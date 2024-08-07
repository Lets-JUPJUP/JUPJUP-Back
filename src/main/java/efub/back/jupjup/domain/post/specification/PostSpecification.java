package efub.back.jupjup.domain.post.specification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;

public class PostSpecification {

	public static Specification<Post> withGender(Boolean allGender, Gender userGender) {
		return (root, query, criteriaBuilder) -> {
			if (Boolean.TRUE.equals(allGender)) {
				return null; // 모든 성별 포함
			}
			// allGender가 false인 경우, 사용자 성별과 ANY만 포함
			return criteriaBuilder.or(
				criteriaBuilder.equal(root.get("postGender"), PostGender.ANY),
				criteriaBuilder.equal(root.get("postGender"), convertGenderToPostGender(userGender))
			);
		};
	}

	private static PostGender convertGenderToPostGender(Gender gender) {
		switch (gender) {
			case FEMALE:
				return PostGender.FEMALE;
			case MALE:
				return PostGender.MALE;
			default:
				return PostGender.ANY;
		}
	}

	public static Specification<Post> withPet(Boolean withPet) {
		return (root, query, criteriaBuilder) ->
			withPet == null ? null : criteriaBuilder.equal(root.get("withPet"), withPet);
	}

	public static Specification<Post> withDistricts(List<District> districts) {
		return (root, query, criteriaBuilder) -> {
			if (districts == null || districts.isEmpty()) {
				return null;
			}
			return root.get("district").in(districts);
		};
	}

	public static Specification<Post> withAgeRange(Boolean allAge, Integer userAge) {
		return (root, query, criteriaBuilder) -> {
			if (Boolean.TRUE.equals(allAge)) {
				return null; // 모든 연령 포함
			}
			if (userAge != null) {
				return criteriaBuilder.and(
					criteriaBuilder.lessThanOrEqualTo(root.get("minAge"), userAge),
					criteriaBuilder.greaterThanOrEqualTo(root.get("maxAge"), userAge)
				);
			}
			return null;
		};
	}

	public static Specification<Post> excludeClosedRecruitment(Boolean exclude) {
		return (root, query, criteriaBuilder) ->
			exclude == null || !exclude ? null :
				criteriaBuilder.greaterThan(root.get("dueDate"), LocalDateTime.now());
	}
}
