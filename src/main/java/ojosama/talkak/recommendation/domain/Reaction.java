package ojosama.talkak.recommendation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reaction {

    private Integer watched;
    private Integer liked;

    public static Reaction createReaction() {
        return new Reaction(1, 0);
    }

    public static Reaction of(Integer watched, Integer liked) {
        return new Reaction(watched, liked);
    }

    public void updateLike() {
        this.liked = Math.abs(liked - 1);
    }

}
