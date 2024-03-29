package efub.back.jupjup.domain.trashCan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.trashCan.domain.BinFeedback;
import efub.back.jupjup.domain.trashCan.domain.Direction;
import efub.back.jupjup.domain.trashCan.domain.Feedback;
import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.dto.Location;
import efub.back.jupjup.domain.trashCan.dto.request.FeedbackReqDto;
import efub.back.jupjup.domain.trashCan.dto.response.FeedbackResDto;
import efub.back.jupjup.domain.trashCan.dto.response.TrashCansResDto;
import efub.back.jupjup.domain.trashCan.exception.TrashCanNotFoundException;
import efub.back.jupjup.domain.trashCan.repository.BinFeedbackRepository;
import efub.back.jupjup.domain.trashCan.repository.TrashCanRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import efub.back.jupjup.global.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrashCanService {
	private static final Double distance500m = 0.4;
	private static final Double distance1km = 1.0;

	private final TrashCanRepository trashCanRepository;
	private final BinFeedbackRepository binFeedbackRepository;
	private final EntityManager em;

	public ResponseEntity<StatusResponse> findNearbyTrashCan(Double mapX, Double mapY) {
		List<TrashCan> trashCans = findNearbyTrashCanWithRadius(mapX, mapY, distance1km);
		TrashCansResDto resDto = new TrashCansResDto(distance1km, trashCans);
		return make200Response(resDto);
	}

	public List<TrashCan> findNearbyTrashCanWithRadius(Double x, Double y, Double distance) {
		Location northEast = GeometryUtil.calculate(x, y, distance, Direction.NORTHEAST.getBearing());
		Location southWest = GeometryUtil.calculate(x, y, distance, Direction.SOUTHWEST.getBearing());

		String pointFormat = String.format(
			"'LINESTRING(%f %f, %f %f)'",
			northEast.getLatitude(), northEast.getLongitude(), southWest.getLatitude(), southWest.getLongitude()
		);
		Query query = em.createNativeQuery(
				"" +
					"SELECT * \n" +
					"FROM trash_can AS c \n" +
					"WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + "), c.location)\n" +
					"ORDER BY ST_DISTANCE(c.location, POINT(:x, :y)) ASC\n" +  // Order by distance ascending
					"LIMIT 20", TrashCan.class)
			.setParameter("x", x)
			.setParameter("y", y);

		List<TrashCan> trashCans = query.getResultList();
		return trashCans;
	}

	public ResponseEntity<StatusResponse> writeFeedback(Member member, FeedbackReqDto feedbackReqDto) {
		if (!trashCanRepository.existsById(feedbackReqDto.getTrashCanId())) {
			throw new TrashCanNotFoundException();
		}
		Feedback feedback = Feedback.getFeedbackByCode(feedbackReqDto.getFeedbackCode());
		BinFeedback savedFeedback = binFeedbackRepository.save(BinFeedback.builder()
			.trashCanId(feedbackReqDto.getTrashCanId())
			.feedback(feedback)
			.member(member)
			.build());
		FeedbackResDto resDto = FeedbackResDto.from(savedFeedback);
		return make200Response(resDto);
	}

	public ResponseEntity<StatusResponse> findFeedbacks(Member member, Long trashcanId) {
		if (!trashCanRepository.existsById(trashcanId)) {
			throw new TrashCanNotFoundException();
		}

		List<BinFeedback> binFeedbacks = binFeedbackRepository.findAllByMemberAndTrashCanId(member, trashcanId);
		Map<Integer, Boolean> feedbackExistsMap = new HashMap<>();
		for (Feedback feedback : Feedback.values()) {
			boolean feedbackExists = binFeedbacks.stream()
				.anyMatch(binFeedback -> binFeedback.getFeedback() == feedback);
			feedbackExistsMap.put(feedback.getCode(), feedbackExists);

		}

		return make200Response(feedbackExistsMap);
	}

	private ResponseEntity<StatusResponse> make200Response(Object obj) {
		return ResponseEntity.ok()
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(obj)
				.build());
	}

}
