package efub.back.jupjup.domain.trashCan.service;

import efub.back.jupjup.domain.trashCan.domain.Direction;
import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.dto.Location;
import efub.back.jupjup.domain.trashCan.repository.TrashCanRepository;
import efub.back.jupjup.global.util.GeometryUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrashCanService {
    private final TrashCanRepository trashCanRepository;
    private final EntityManager em;

    public List<TrashCan> findNearbyTrashCan(Double x, Double y) {
//        Double x = Double.valueOf(mapX);
//        Double y = Double.valueOf(mapY);
        Location northEast = GeometryUtil.calculate(x, y, 0.5, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(x, y, 0.5, Direction.SOUTHWEST.getBearing());

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

//        List<TrashCan> trashCans = trashCanRepository.findWithinMap(southWest.getLatitude(), northEast.getLatitude(),
//                southWest.getLongitude(),
//                northEast.getLongitude());

        return trashCans;
    }
}
