package efub.back.jupjup.domain.trashCan.service;

import efub.back.jupjup.domain.trashCan.domain.Direction;
import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.dto.Location;
import efub.back.jupjup.domain.trashCan.dto.TrashCansResDto;
import efub.back.jupjup.domain.trashCan.repository.TrashCanRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import efub.back.jupjup.global.util.GeometryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrashCanService {
    private static final Double distance500m = 0.5;
    private static final Double distance1km = 1.0;

    private final TrashCanRepository trashCanRepository;
    private final EntityManager em;

    public ResponseEntity<StatusResponse> findNearbyTrashCan(Double mapX, Double mapY) {
        List<TrashCan> trashCans;
        trashCans = findNearbyTrashCanWithRadius(mapX, mapY, distance500m);
        if (trashCans.size() < 20) {
            trashCans = findNearbyTrashCanWithRadius(mapX, mapY, distance1km);
            TrashCansResDto resDto = new TrashCansResDto(distance1km, trashCans);
            return make200Response(resDto);
        }
        TrashCansResDto resDto = new TrashCansResDto(distance500m, trashCans);
        return make200Response(resDto);
    }

    public List<TrashCan> findNearbyTrashCanWithRadius(Double x, Double y, Double distance){
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
                        "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + "), c.location)"
                , TrashCan.class);
        List<TrashCan> trashCans = query.getResultList();
        return trashCans;
    }

    private ResponseEntity<StatusResponse> make200Response(Object obj){
        return ResponseEntity.ok()
                .body(StatusResponse.builder()
                        .status(StatusEnum.OK.getStatusCode())
                        .message(StatusEnum.OK.getCode())
                        .data(obj)
                        .build());
    }
}
