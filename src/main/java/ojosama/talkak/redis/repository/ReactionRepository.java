package ojosama.talkak.redis.repository;

import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.Reaction;
import ojosama.talkak.redis.innerkey.ReactionHashKey;
import ojosama.talkak.redis.key.ReactionKey;
import org.springframework.stereotype.Repository;

@Repository
public class ReactionRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public ReactionRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public Reaction save(Long memberId, Long videoId, Reaction reaction) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashOps(key, hashConverter.toMap(reaction));
        return hashConverter.FromMap(redisService.getHashOps(key), Reaction.class);
    }

    public Reaction findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        return hashConverter.FromMap(redisService.getHashOps(key), Reaction.class);
    }

    public Reaction updateLiked(Long memberId, Long videoId, Integer liked) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashValue(key, ReactionHashKey.LIKED.getKey(), liked);
        return hashConverter.FromMap(redisService.getHashOps(key), Reaction.class);
    }

    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.deleteHash(key);
    }
}
