package efub.back.jupjup.domain.post.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;

public class PostSpecification {

	public static Specification<Post> withGender(Boolean includeAllGenders, Boolean includeUserGender, Gender userGender) {
		return (root, query, criteriaBuilder) -> {
			if (Boolean.TRUE.equals(includeAllGenders)) {
				return null; // 모든 성별 포함
			}
			if (Boolean.TRUE.equals(includeUserGender)) {
				return criteriaBuilder.or(
					criteriaBuilder.equal(root.get("postGender"), PostGender.ANY),
					criteriaBuilder.equal(root.get("postGender"), convertGenderToPostGender(userGender))
				);
			}
			// 기본적으로 ANY만 포함
			return criteriaBuilder.equal(root.get("postGender"), PostGender.ANY);
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

	public static Specification<Post> withDistrict(District district) {
		return (root, query, criteriaBuilder) ->
			district == null ? null : criteriaBuilder.equal(root.get("district"), district);
	}

	public static Specification<Post> withAgeRange(Boolean includeAllAges, Boolean includeUserAge, Integer userAge) {
		return (root, query, criteriaBuilder) -> {
			if (Boolean.TRUE.equals(includeAllAges)) {
				return null; // 모든 연령 포함
			}
			if (Boolean.TRUE.equals(includeUserAge) && userAge != null) {
				return criteriaBuilder.and(
					criteriaBuilder.lessThanOrEqualTo(root.get("minAge"), userAge),
					criteriaBuilder.greaterThanOrEqualTo(root.get("maxAge"), userAge)
				);
			}
			// 기본적으로 모든 연령 포함
			return null;
		};
	}

	public static Specification<Post> excludeClosedRecruitment(Boolean exclude) {
		return (root, query, criteriaBuilder) ->
			exclude == null || !exclude ? null :
				criteriaBuilder.greaterThan(root.get("dueDate"), LocalDateTime.now());
	}
}
