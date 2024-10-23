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

    public static EventQueueDto of(Long memberId, Long categoryId, LocalDateTime lastUpdatedAt) {
        return new EventQueueDto(memberId, categoryId, HashConverter.fromDateTime(lastUpdatedAt));
    }
}
