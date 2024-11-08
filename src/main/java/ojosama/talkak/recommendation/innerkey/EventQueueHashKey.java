package ojosama.talkak.recommendation.innerkey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventQueueHashKey {
    MEMBER_ID("member_id"),
    CATEGORY_ID("category_id"),
    LAST_UPDATED_AT("last_updated_at");

    private final String key;
}
