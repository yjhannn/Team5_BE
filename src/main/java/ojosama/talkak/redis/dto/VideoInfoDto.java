package ojosama.talkak.redis.dto;

import static ojosama.talkak.redis.HashConverter.fromDateTime;

import java.time.LocalDateTime;

public record VideoInfoDto(
    String createdAt,
    Long viewCount,
    Long likeCount
) {

    public static VideoInfoDto createVideoInfo(LocalDateTime createdAt) {
        return new VideoInfoDto(fromDateTime(createdAt),
            0L, 0L);
    }

    public static VideoInfoDto of(LocalDateTime createdAt, Long viewCount, Long likeCount) {
        return new VideoInfoDto(fromDateTime(createdAt),
            viewCount, likeCount);
    }

}
