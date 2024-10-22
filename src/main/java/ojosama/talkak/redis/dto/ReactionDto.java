package ojosama.talkak.redis.dto;

public record ReactionDto(
    Integer watched,
    Integer liked
) {

    public static ReactionDto createReaction() {
        return new ReactionDto(1, 0);
    }

    public static ReactionDto of(Integer watched, Integer liked) {
        return new ReactionDto(watched, liked);
    }

}
