package ojosama.talkak.redis.dto;

public record ScoresDto(
    Long categoryId,
    Long videoId,
    Float scores
) {

    public static ScoresDto of(Long categoryId, Long videoId, Float scores) {
        return new ScoresDto(categoryId, videoId, scores);
    }
}
