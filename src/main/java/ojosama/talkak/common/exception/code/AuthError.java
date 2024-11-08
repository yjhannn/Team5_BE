package ojosama.talkak.common.exception.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthError implements ErrorCode {

    /* 401 Unauthorized */
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 refresh token입니다.");

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