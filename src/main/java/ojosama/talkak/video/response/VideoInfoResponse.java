package ojosama.talkak.video.response;

import java.time.LocalDateTime;

public record VideoInfoResponse(
    Long videoId,
    String thumbnail,
    String title,
    Long memberId,
    LocalDateTime createdAt
    ) {

}
