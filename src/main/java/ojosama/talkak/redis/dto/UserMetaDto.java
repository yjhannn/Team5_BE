package ojosama.talkak.redis.dto;

import static ojosama.talkak.redis.HashConverter.fromDateTime;

import java.time.LocalDateTime;

public record UserMetaDto(
    String lastUpdatedAt
) {

    public static UserMetaDto createUserMeta() {
        return new UserMetaDto(fromDateTime(LocalDateTime.now()));
    }

    public static UserMetaDto of(LocalDateTime lastUpdatedAt) {
        return new UserMetaDto(fromDateTime(lastUpdatedAt));
    }

}
