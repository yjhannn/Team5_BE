package ojosama.talkak.recommendation.innerkey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ScoresSetKey {
    SCORE("%s");

    private final String key;

    public String generateKey(Long videoId) {
        return String.format(key, videoId);
    }

    public Long getVideoId(String key) {
        return Long.valueOf(key);
    }
}
