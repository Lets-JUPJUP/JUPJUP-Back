package efub.back.jupjup.domain.review.repository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.review.domain.Review;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
