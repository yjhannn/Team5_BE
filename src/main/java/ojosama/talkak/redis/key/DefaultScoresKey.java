package ojosama.talkak.redis.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DefaultScoresKey {
    SCORES("scores-default:%s");

    private final String key;

    public String generateKey(Long categoryId) {
        return String.format(key, categoryId);
    }

    public Long getCategoryId(Long categoryId) {
        return Long.parseLong(key.split(":")[1]);
    }

}
