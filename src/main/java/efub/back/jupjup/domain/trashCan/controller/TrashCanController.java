package efub.back.jupjup.domain.trashCan.controller;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.service.TrashCanService;
import efub.back.jupjup.global.response.StatusResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trashCans")
@RequiredArgsConstructor
public class TrashCanController {
    private final TrashCanService trashCanService;

    @GetMapping
    public ResponseEntity<StatusResponse> findNearbyTrashCan(@RequestParam(value = "mapX") Double mapX, @RequestParam(value = "mapY") Double mapY){
        return trashCanService.findNearbyTrashCan(mapX, mapY);
    }
}
