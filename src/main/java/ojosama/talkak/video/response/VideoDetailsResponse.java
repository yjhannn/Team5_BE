package ojosama.talkak.video.response;

public record VideoDetailsResponse(
    Long id, String videoUrl, MemberInfoResponse member, Long likesCount, int commentNumber
) {

}
