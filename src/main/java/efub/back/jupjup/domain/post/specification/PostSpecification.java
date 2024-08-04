package efub.back.jupjup.domain.post.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;

public class PostSpecification {

	public static Specification<Post> withGender(PostGender userGender) {
		return (root, query, criteriaBuilder) ->
			userGender == null ? null : criteriaBuilder.or(
				criteriaBuilder.equal(root.get("postGender"), PostGender.ANY),
				criteriaBuilder.equal(root.get("postGender"), userGender)
			);
	}

	public static Specification<Post> withPet(Boolean withPet) {
		return (root, query, criteriaBuilder) ->
			withPet == null ? null : criteriaBuilder.equal(root.get("withPet"), withPet);
	}

	public static Specification<Post> withDistrict(District district) {
		return (root, query, criteriaBuilder) ->
			district == null ? null : criteriaBuilder.equal(root.get("district"), district);
	}

	public static Specification<Post> withAgeRange(Integer minAge, Integer maxAge) {
		return (root, query, criteriaBuilder) -> {
			if (minAge == null && maxAge == null) return null;
			if (minAge == null) return criteriaBuilder.lessThanOrEqualTo(root.get("maxAge"), maxAge);
			if (maxAge == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("minAge"), minAge);
			return criteriaBuilder.and(
				criteriaBuilder.greaterThanOrEqualTo(root.get("minAge"), minAge),
				criteriaBuilder.lessThanOrEqualTo(root.get("maxAge"), maxAge)
			);
		};
	}

	public static Specification<Post> excludeClosedRecruitment(Boolean exclude) {
		return (root, query, criteriaBuilder) ->
			exclude == null || !exclude ? null :
				criteriaBuilder.greaterThan(root.get("dueDate"), LocalDateTime.now());
	}
}
