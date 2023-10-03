package efub.back.jupjup.domain.demo.controller;

import efub.back.jupjup.domain.auth.dto.DemoResDto;
import efub.back.jupjup.domain.demo.service.DemoService;
import efub.back.jupjup.domain.member.exception.InvalidNicknameException;
import efub.back.jupjup.domain.security.exception.EmptyTokenException;
import efub.back.jupjup.global.exception.custom.ApplicationException;
import efub.back.jupjup.global.exception.dto.ErrorResponse;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "무슨무슨 기능 API", description = "API 관련 설명")
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @Operation(summary = "무슨무슨 API", description = "해당 API에 대한 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = DemoResDto.class))),
            @ApiResponse(responseCode = "400", description = "C1000 : 유효하지 않은 닉네임입니다 \t\n C1001: 에러 케이스 설명~" , content = @Content)
    })
    @GetMapping("/test")
    public ResponseEntity<StatusResponse> testSwagger(@Parameter(name = "str1", description = "쿼리파라미터 1 설명") @RequestParam String str1){
        if(str1.length() > 5){
            throw new InvalidNicknameException();
        }
        return demoService.demoMethod(str1);
    }

    @Hidden
    @GetMapping("/ignore")
    public String ingore(){
        return "swagger에서 무시되는 API";
    }
}
