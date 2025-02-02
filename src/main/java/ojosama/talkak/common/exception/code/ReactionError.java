package ojosama.talkak.common.exception.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReactionError implements ErrorCode {
    UNAUTHORIZED_USER(HttpStatus.BAD_REQUEST, "R001", "인증되지 않은 유저입니다."),
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "R002", "유효하지 않은 memberId 입니다."),
    INVALID_VIDEO_ID(HttpStatus.BAD_REQUEST, "R003", "유효하지 않은 videoID 입니다."),
    FAILED_PROCESS_REQUEST(HttpStatus.BAD_REQUEST, "R004", "좋아요 요청 처리에 실패하였습니다. 다시 시도해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
