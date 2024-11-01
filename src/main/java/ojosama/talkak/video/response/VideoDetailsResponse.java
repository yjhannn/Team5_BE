package ojosama.talkak.video.response;

public record VideoDetailsResponse(
    Long id, Long categoryId, String videoUrl, MemberInfoResponse member, Long likesCount, Long viewsCount, int commentNumber
) {

}
