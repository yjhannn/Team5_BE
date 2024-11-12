package ojosama.talkak.video.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import ojosama.talkak.common.util.LocalDateTimeSerializer;

public record VideoInfoResponse(
    Long videoId,
    String thumbnail,
    String title,
    Long memberId,
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createdAt
    ) {

}
