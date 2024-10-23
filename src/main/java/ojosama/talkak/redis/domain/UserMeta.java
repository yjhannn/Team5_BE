package ojosama.talkak.redis.domain;

import static ojosama.talkak.redis.HashConverter.fromDateTime;

import java.time.LocalDateTime;

public record UserMeta(
    String lastUpdatedAt
) {

    public static UserMeta createUserMeta() {
        return new UserMeta(fromDateTime(LocalDateTime.now()));
    }

    public static UserMeta of(LocalDateTime lastUpdatedAt) {
        return new UserMeta(fromDateTime(lastUpdatedAt));
    }

}
