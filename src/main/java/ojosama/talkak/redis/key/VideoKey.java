package ojosama.talkak.redis.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VideoKey {
    VIDEO_INFO("video_%s:%s");

    private final String key;

    public String generateKey(Long categoryId, Long videoId) {
        return String.format(key, categoryId, videoId);
    }

    public Long getCategoryId() {
        return
            Long.parseLong(key.split(":")[1]);
    }

    public Long getVideoId() {
        return Long.parseLong(key.split(":")[2]);
    }
}
