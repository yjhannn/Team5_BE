package ojosama.talkak.redis.repository;

import java.util.List;
import java.util.Set;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.Scores;
import ojosama.talkak.redis.innerkey.ScoresSetKey;
import ojosama.talkak.redis.key.DefaultScoresKey;
import ojosama.talkak.redis.key.ScoresKey;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Repository
public class ScoresRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public ScoresRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public List<Scores> findDefaultTopRank(Long categoryId, long count) {
        String key = DefaultScoresKey.SCORES.generateKey(categoryId);
        return getScoresList(categoryId, count, key);
    }

    public List<Scores> findTopRankScores(Long memberId, Long categoryId, long count) {
        String key = ScoresKey.SCORES.generateKey(categoryId, memberId);
        return getScoresList(categoryId, count, key);
    }

    private List<Scores> getScoresList(Long categoryId, long count, String key) {
        Set<TypedTuple<Object>> tuple = redisService.getSortedSetOps(key, count);

        return tuple.stream()
            .filter(t -> t.getValue() != null && t.getScore() != null)
            .map(t -> Scores.of(categoryId,
                ScoresSetKey.SCORE.getVideoId(t.getValue().toString()),
                t.getScore().floatValue()
            ))
            .toList();
    }

}
