package efub.back.jupjup.domain.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import efub.back.jupjup.config.WithAuthUser;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.dto.request.MemberReqDto;
import efub.back.jupjup.domain.member.dto.request.NicknameCheckReqDto;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest extends PreProcessController {
	private static final String USER_EMAIL = "gy5027@naver.com";
	private static final String USER_NICKNAME = "tester";
	private static final String USER_ROLE = "MEMBER";
	private static final String USER_GENDER = "WOMAN";
	private static final String USER_PROFILE_IMAGE = "https://";
	private static final String USER_AGE_RANGE = "AGE_20_29";

	@BeforeEach
	void setUp() {
		loginMockSetUp();
	}

	@Test
	@WithAuthUser
	@DisplayName("프로필 조회 성공")
	void readProfileTest() throws Exception {

		// given
		willReturn(ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.build())).given(memberService).readProfile(
			anyLong());
		// when
		RequestBuilder requestBuilder = get(String.format("/api/v1/members/%d", 1L))
			.header(HttpHeaders.AUTHORIZATION, "token");

		ResultActions perform = mockMvc.perform(requestBuilder);

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@WithAuthUser
	@DisplayName("프로필 업데이트 성공")
	void getMyProfileSuccess() throws Exception {

		// given
		MemberReqDto memberReqDto = new MemberReqDto(USER_NICKNAME, USER_GENDER, USER_AGE_RANGE, USER_PROFILE_IMAGE);
		willReturn(ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.build()))
			.given(memberService).updateProfile(any(Member.class), any(MemberReqDto.class));

		// when
		RequestBuilder requestBuilder = put("/api/v1/members")
			.header(HttpHeaders.AUTHORIZATION, "token")
			.content(objectMapper.writeValueAsString(memberReqDto))
			.contentType(MediaType.APPLICATION_JSON);
		ResultActions perform = mockMvc.perform(requestBuilder);

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@WithAuthUser
	@DisplayName("닉네임 중복 체크 성공")
	void checkNicknameSuccess() throws Exception {

		// given
		NicknameCheckReqDto nicknameCheckReqDto = new NicknameCheckReqDto(USER_NICKNAME);
		willReturn(ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.build()))
			.given(memberService).checkDuplicateNickname(any(NicknameCheckReqDto.class), any(Member.class));

		// when
		RequestBuilder requestBuilder = post("/api/v1/members/checkNickname")
			.header(HttpHeaders.AUTHORIZATION, "token")
			.content(objectMapper.writeValueAsString(nicknameCheckReqDto))
			.contentType(MediaType.APPLICATION_JSON);
		ResultActions perform = mockMvc.perform(requestBuilder);

		// then
		perform.andExpect(status().isOk());
	}

}
