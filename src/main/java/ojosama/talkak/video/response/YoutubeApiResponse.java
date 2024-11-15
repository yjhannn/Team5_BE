package ojosama.talkak.video.response;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import ojosama.talkak.common.util.LocalDateTimeSerializer;

public record YoutubeApiResponse(
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime date,
    String videoId,
    String channelId,
    String title,
    String thumbnailUrl) {

}
