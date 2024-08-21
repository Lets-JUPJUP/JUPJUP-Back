package efub.back.jupjup.domain.trashCan.service;

import java.util.List;
import java.util.Optional;

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
import efub.back.jupjup.domain.trashCan.exception.FeedbackAlreadyExistsException;
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

		// 경도와 위도를 맞바꿔야 합니다.
		String pointFormat = String.format(
			"'LINESTRING(%f %f, %f %f)'",
			northEast.getLongitude(), northEast.getLatitude(), southWest.getLongitude(), southWest.getLatitude()
		);

		Query query = em.createNativeQuery(
				"SELECT * \n" +
					"FROM trash_can AS c \n" +
					"WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + "), c.location)\n" +
					"ORDER BY ST_DISTANCE(c.location, POINT(:longitude, :latitude)) ASC\n" +  // 경도, 위도 순서로 지정
					"LIMIT 20", TrashCan.class)
			.setParameter("longitude", y)  // 경도
			.setParameter("latitude", x);  // 위도

		List<TrashCan> trashCans = query.getResultList();
		System.out.println(trashCans.size());
		return trashCans;
	}

	public ResponseEntity<StatusResponse> writeFeedback(Member member, FeedbackReqDto feedbackReqDto) {
		if (!trashCanRepository.existsById(feedbackReqDto.getTrashCanId())) {
			throw new TrashCanNotFoundException();
		}

		Optional<BinFeedback> binFeedbackOptional = binFeedbackRepository.findBinFeedbackByMemberAndTrashCanId(member,
			feedbackReqDto.getTrashCanId());
		BinFeedback binFeedback;
		Feedback newFeedback = Feedback.getFeedbackByCode(feedbackReqDto.getFeedbackCode());

		if (binFeedbackOptional.isPresent()) {
			binFeedback = binFeedbackOptional.get();
			// 동일한 피드백을 보내는 경우
			if (binFeedback.getFeedback().equals(newFeedback)) {
				throw new FeedbackAlreadyExistsException();
			}
			binFeedback.updateFeedback(newFeedback);
		} else {
			binFeedback = BinFeedback.builder()
				.trashCanId(feedbackReqDto.getTrashCanId())
				.feedback(newFeedback)
				.member(member)
				.build();
		}

		BinFeedback savedBinFeedback = binFeedbackRepository.save(binFeedback);
		FeedbackResDto resDto = FeedbackResDto.from(savedBinFeedback);
		return make200Response(resDto);
	}

	public ResponseEntity<StatusResponse> findFeedback(Member member, Long trashcanId) {
		if (!trashCanRepository.existsById(trashcanId)) {
			throw new TrashCanNotFoundException();
		}

		FeedbackResDto binFeedback = binFeedbackRepository.findBinFeedbackByMemberAndTrashCanId(member, trashcanId)
			.map(FeedbackResDto::from)
			.orElseGet(() -> {
				return FeedbackResDto.builder()
					.id(null)
					.trashCanId(trashcanId)
					.feedbackCode(Feedback.UNDEFINED.getCode())
					.feedback(null)
					.memberId(member.getId())
					.createdAt(null)
					.build();
			});

		return make200Response(binFeedback);
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
