package ojosama.talkak.redis.dto;

public record ScoresDto(
    Float score
) {

    public static ScoresDto createScores() {
        return new ScoresDto(0f);
    }

    public static ScoresDto of(Float score) {
        return new ScoresDto(score);
    }

}
