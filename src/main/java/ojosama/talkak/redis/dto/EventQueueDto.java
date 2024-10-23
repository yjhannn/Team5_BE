package ojosama.talkak.redis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import ojosama.talkak.redis.HashConverter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EventQueueDto(
    Long memberId,
    Long categoryId,
    String lastUpdatedAt
) {

    public static EventQueueDto of(Long userId, Long categoryId, LocalDateTime lastUpdatedAt) {
        return new EventQueueDto(userId, categoryId, HashConverter.fromDateTime(lastUpdatedAt));
    }
}
