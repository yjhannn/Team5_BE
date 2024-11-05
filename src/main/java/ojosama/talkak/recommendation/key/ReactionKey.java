package ojosama.talkak.recommendation.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReactionKey {
    REACTION("reaction:%s:%s");

    private final String key;

    public String generateKey(Long memberId, Long videoId) {
        return String.format(key, memberId, videoId);
    }

    public Long getMemberId() {
        return Long.parseLong(key.split(":")[1]);
    }

    public Long getVideoId() {
        return Long.parseLong(key.split(":")[2]);
    }
}
