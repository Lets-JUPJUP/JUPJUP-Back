package efub.back.jupjup.global.exception;

import java.util.Arrays;
import java.util.Objects;

import efub.back.jupjup.domain.admin.member.exception.AdminAccessDeniedException;
import efub.back.jupjup.domain.auth.exception.AlreadyLogoutException;
import efub.back.jupjup.domain.auth.exception.RefreshTokenNotValidException;
import efub.back.jupjup.domain.comment.exception.NoAuthorityCommentRemoveException;
import efub.back.jupjup.domain.comment.exception.NoCommentExistsException;
import efub.back.jupjup.domain.eventcomment.exception.NoEventInfoException;
import efub.back.jupjup.domain.member.exception.InvalidNicknameException;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.notification.exception.NotificationNotFoundException;
import efub.back.jupjup.domain.post.exception.EmptyInputFilenameException;
import efub.back.jupjup.domain.post.exception.MaxMemberLimitException;
import efub.back.jupjup.domain.post.exception.MismatchPostAgeRangeException;
import efub.back.jupjup.domain.post.exception.MismatchPostGenderException;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.exception.PostjoinNotFoundException;
import efub.back.jupjup.domain.post.exception.WrongImageFormatException;
import efub.back.jupjup.domain.review.exception.BadgeNotExistsForCodeException;
import efub.back.jupjup.domain.review.exception.ReviewNotAllowedException;
import efub.back.jupjup.domain.security.exception.BlockedAccountException;
import efub.back.jupjup.domain.security.exception.ExpiredTokenException;
import efub.back.jupjup.domain.trashCan.exception.FeedbackNotExistsForCodeException;
import efub.back.jupjup.domain.trashCan.exception.TrashCanNotFoundException;
import efub.back.jupjup.global.exception.custom.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionType {

	// 서버 자체 관련 - C0***
	UNHANDLED_EXCEPTION("C0000", "알 수 없는 서버 에러가 발생했습니다."),
	METHOD_ARGUMENT_NOT_VALID_EXCEPTION("C0001", "요청 데이터가 잘못되었습니다."),

	//회원 관련 - C1***
	INVALID_NICKNAME_EXCEPTION("C1000", "유효하지 않은 닉네임입니다.", InvalidNicknameException.class),
	MEMBER_NOT_FOUND_EXCEPTION("C1001", "존재하지 않는 멤버입니다.", MemberNotFoundException.class),
	BLOCKED_ACCOUNT_EXCEPTION("C1002", "관리자에 의해 탈퇴된 계정입니다.", BlockedAccountException.class),

	//auth 관련 - C2***
	REFRESH_TOKEN_NOT_VALID_EXCEPTION("C2000", "redis의 리프레시 토큰과 일치하지 않습니다.", RefreshTokenNotValidException.class),
	ALREADY_LOGOUT_EXCEPTION("C2001", "이미 로그아웃된 회원입니다.", AlreadyLogoutException.class),
	EXPIRED_TOKEN_EXCEPTION("C2002", "만료된 토큰입니다.", ExpiredTokenException.class),

	// review 관련 - C3***
	BADGE_NOT_EXISTS_FOR_CODE_EXCEPTION("C3000", "해당 코드와 일치하는 뱃지가 존재하지 않습니다.", BadgeNotExistsForCodeException.class),
	REVIEW_NOT_ALLOWED_EXCEPTION("C3001", "참여 멤버가 아니면 리뷰를 작성할 수 없습니다.", ReviewNotAllowedException.class),

	// trashCan 관련 - C4***
	FEEDBACK_NOT_EXISTS_FOR_CODE_EXCEPTION("C4000", "해당 코드와 일치하는 쓰레기통 피드백이 존재하지 않습니다.",
		FeedbackNotExistsForCodeException.class),
	TRASHCAN_NOT_FOUND_EXCEPTION("C4001", "존재하지 않는 쓰레기통입니다.", TrashCanNotFoundException.class),

	// 알림 관련 - C5***
	NOTIFICATION_NOT_FOUND_EXCEPTION("C5000", "해당 알림을 찾을 수 없습니다.", NotificationNotFoundException.class),

	// 게시글 관련 - C6***
	POST_NOT_FOUND_EXCEPTION("C6000", "존재하지 않는 게시글입니다.", PostNotFoundException.class),
	MAX_MEMBER_LIMIT_EXCEPTION("C6001", "최대 인원을 초과하여 참여할 수 없습니다.", MaxMemberLimitException.class),
	MISMATCH_POST_GENDER_EXCEPTION("C6002", "성별이 해당 플로깅의 조건과 맞지 않습니다.", MismatchPostGenderException.class),
	MISMATCH_POST_AGE_RANGE_EXCEPTION("C6003", "나이가 해당 플로깅의 조건과 맞지 않습니다.", MismatchPostAgeRangeException.class),
	POST_JOIN_NOT_FOUND_EXCEPTION("C6004", "게시글 참여 정보가 없습니다.", PostjoinNotFoundException.class),

	// 관리자 관련 - C7***
	ADMIN_ACCESS_DENIED_EXCEPTION("C7000", "관리자가 아닌 계정은 접근할 수 없습니다.", AdminAccessDeniedException.class),

	// 이미지 관련 - C8***
	EMPTY_INPUT_FILENAME_EXCEPTION("C8000", "파일명이 비어있습니다.", EmptyInputFilenameException.class),
	WRONG_IMAGE_FORMAT_EXCEPTION("C8001", "옳지 않은 파일 형식입니다.", WrongImageFormatException.class),

	// 댓글 관련 - C9***
	NO_AUTHORITY_COMMENT_REMOVE_EXCEPTION("C9000", "댓글을 삭제할 권한이 없습니다", NoAuthorityCommentRemoveException.class),
	NO_COMMENT_EXISTS_EXCEPTION("C9001", "존재하지 않는 댓글입니다.", NoCommentExistsException.class),

	// 공식행사 관련 - C10***
	NO_EVENT_INFO_EXCEPTION("C10000", "존재하지 않는 공식행사입니다.", NoEventInfoException.class);


	private final String errorCode;
	private final String message;
	private Class<? extends ApplicationException> type;

	ExceptionType(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public static ExceptionType from(Class<?> classType) {
		return Arrays.stream(values())
			.filter(it -> Objects.nonNull(it.type) && it.type.equals(classType))
			.findFirst()
			.orElse(UNHANDLED_EXCEPTION);
	}
}
