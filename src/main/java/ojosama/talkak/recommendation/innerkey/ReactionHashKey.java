package ojosama.talkak.recommendation.innerkey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReactionHashKey {
    WATCHED("watched"),
    LIKED("liked");

    private final String key;
}
