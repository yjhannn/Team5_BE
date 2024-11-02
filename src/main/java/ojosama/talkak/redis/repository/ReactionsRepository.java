package ojosama.talkak.redis.repository;

import java.util.Map;
import java.util.Optional;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.Reaction;
import ojosama.talkak.redis.key.ReactionKey;
import org.springframework.stereotype.Repository;

@Repository
public class ReactionsRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public ReactionsRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public Reaction save(Long memberId, Long videoId, Reaction reaction) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashOps(key, hashConverter.toMap(reaction));
        return hashConverter.FromMap(redisService.getHashOps(key), Reaction.class);
    }

    public Optional<Reaction> findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        Map<String, Object> entries = redisService.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, Reaction.class));
    }


    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.deleteHash(key);
    }
}
