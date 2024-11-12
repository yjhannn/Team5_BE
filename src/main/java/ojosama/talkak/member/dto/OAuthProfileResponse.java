package ojosama.talkak.member.dto;

public record OAuthProfileResponse(
    String username,
    String imageUrl
) {

    public static OAuthProfileResponse of(String username, String imageUrl) {
        return new OAuthProfileResponse(username, imageUrl);
    }
}
