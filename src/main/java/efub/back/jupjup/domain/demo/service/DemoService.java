package efub.back.jupjup.domain.demo.service;

import efub.back.jupjup.domain.auth.dto.DemoResDto;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DemoService {
    public ResponseEntity<StatusResponse> demoMethod(String str1){
        DemoResDto resDto = DemoResDto.builder()
                .userId(str1)
                .userName("홍길동")
                .build();

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDto) //DTO, String 같은 객체이기만 하면 OK
                .build());
    }
}
