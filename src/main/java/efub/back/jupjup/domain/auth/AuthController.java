package efub.back.jupjup.domain.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증.인가 API", description = "카카오 로그인을 통한 인증.인가 API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Operation(summary = "어떤 기능을 하는지 설명합니다.")
    @Parameter(name = "str1", description = "쿼리파라미터 1")
    @GetMapping("/test")
    public String testSwagger(@RequestParam String str1){
        return str1 + "\n" + str1;
    }

    @Hidden
    @GetMapping("/ignore")
    public String ingore(){
        return "swagger에서 무시되는 API";
    }
}
