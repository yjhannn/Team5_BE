package ojosama.talkak.recommendation.innerkey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserMetaHashKey {
    LAST_UPDATED_AT("last_updated_at");

    private final String key;
}
