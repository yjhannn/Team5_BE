package ojosama.talkak.member.dto;

public record ProfileResponse(
    Long memberId,
    String email,
    String username,
    String imageUrl
) {

    public static ProfileResponse of(Long memberId, String email, String username, String imageUrl) {
        return new ProfileResponse(memberId, email, username, imageUrl);
    }
}
