package ojosama.talkak.common.exception.code;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthError implements ErrorCode {

    /* 401 Unauthorized */
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 refresh token입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "토큰을 올바르게 입력해주세요.");

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

    public static AuthError from(Exception exception) {
        return Optional.ofNullable(exception)
            .map(e -> {
                if (e instanceof ExpiredJwtException) {
                    return EXPIRED_TOKEN;
                }
                return INVALID_TOKEN;
            })
            .orElse(INVALID_TOKEN);
    }
}