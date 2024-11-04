package ojosama.talkak.recommendation.domain;

public record Scores(
    Long categoryId,
    Long videoId,
    Float scores
) {

    public static Scores of(Long categoryId, Long videoId, Float scores) {
        return new Scores(categoryId, videoId, scores);
    }
}
