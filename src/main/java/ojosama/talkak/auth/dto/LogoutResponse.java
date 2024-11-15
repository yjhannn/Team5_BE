package ojosama.talkak.auth.dto;

import org.springframework.http.HttpStatus;

public record LogoutResponse(
    String message,
    String data
) {

    public static LogoutResponse of(String message, HttpStatus status) {
        return new LogoutResponse(message, status.value() + " " + status.getReasonPhrase());
    }
}
