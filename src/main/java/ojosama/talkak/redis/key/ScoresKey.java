package ojosama.talkak.redis.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ScoresKey {
    SCORES("scores:%s:%s");

    private final String key;

    public String generateKey(Long categoryId, Long memberId) {
        return String.format(key, categoryId, memberId);
    }

    public Long getCategoryId(Long categoryId) {
        return Long.parseLong(key.split(":")[1]);
    }

    public Long getMemberId() {
        return Long.parseLong(key.split(":")[2]);
    }
}
