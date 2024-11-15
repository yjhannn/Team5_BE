package ojosama.talkak.auth.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    Boolean isFreshUser
) {

    public static TokenResponse of(String accessToken, String refreshToken, Boolean isFreshUser) {
        return new TokenResponse(accessToken, refreshToken, isFreshUser);
    }
}
