package ojosama.talkak.recommendation.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserMetaKey {
    USER_META("user_meta:%s");

    private final String key;

    public String generateKey(Long memberId) {
        return String.format(key, memberId);
    }

    public Long getMemberId() {
        return Long.parseLong(key.split(":")[1]);
    }
}
