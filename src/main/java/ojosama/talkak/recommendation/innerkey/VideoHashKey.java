package ojosama.talkak.recommendation.innerkey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VideoHashKey {
    CREATED_AT("created_at"),
    VIEW_COUNT("view_count"),
    LIKE_COUNT("like_count");

    private final String key;
}
