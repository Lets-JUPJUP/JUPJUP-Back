package efub.back.jupjup.domain.post.specification;

import org.springframework.data.jpa.domain.Specification;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;

public class PostSpecification {

	public static Specification<Post> withGender(PostGender gender) {
		return (root, query, criteriaBuilder) ->
			gender == null ? null : criteriaBuilder.equal(root.get("postGender"), gender);
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
}
