package ojosama.talkak.redis.domain;

import static ojosama.talkak.redis.HashConverter.fromDateTime;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoInfo {

    private String createdAt;
    private Long viewCount;
    private Long likeCount;

    public static VideoInfo createVideoInfo(LocalDateTime createdAt) {
        return new VideoInfo(fromDateTime(createdAt),
            0L, 0L);
    }

    public static VideoInfo of(LocalDateTime createdAt, Long viewCount, Long likeCount) {
        return new VideoInfo(fromDateTime(createdAt),
            viewCount, likeCount);
    }

}
