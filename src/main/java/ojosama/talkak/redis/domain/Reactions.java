package ojosama.talkak.redis.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reactions {

    private Integer watched;
    private Integer liked;

    public static Reactions createReaction() {
        return new Reactions(1, 0);
    }

    public static Reactions of(Integer watched, Integer liked) {
        return new Reactions(watched, liked);
    }

    public void updateLike() {
        this.liked = Math.abs(liked - 1);
    }

}
