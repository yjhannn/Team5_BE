package ojosama.talkak.redis.repository;

import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.Reactions;
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

    public Reactions save(Long memberId, Long videoId, Reactions reactions) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashOps(key, hashConverter.toMap(reactions));
        return hashConverter.FromMap(redisService.getHashOps(key), Reactions.class);
    }

    public Reactions findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        return hashConverter.FromMap(redisService.getHashOps(key), Reactions.class);
    }

    public Reactions updateLiked(Long memberId, Long videoId, Integer liked) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashValue(key, ReactionHashKey.LIKED.getKey(), liked);
        return hashConverter.FromMap(redisService.getHashOps(key), Reactions.class);
    }

    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.deleteHash(key);
    }
}
