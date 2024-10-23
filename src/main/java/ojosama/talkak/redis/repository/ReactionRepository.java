package ojosama.talkak.redis.repository;

import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.dto.ReactionDto;
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

    public ReactionDto save(Long memberId, Long videoId, ReactionDto reactionDto) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashOps(key, hashConverter.toMap(reactionDto));
        return hashConverter.FromMap(redisService.getHashOps(key), ReactionDto.class);
    }

    public ReactionDto findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        return hashConverter.FromMap(redisService.getHashOps(key), ReactionDto.class);
    }

    public ReactionDto updateLiked(Long memberId, Long videoId, Integer liked) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.setHashValue(key, ReactionHashKey.LIKED.getKey(), liked);
        return hashConverter.FromMap(redisService.getHashOps(key), ReactionDto.class);
    }

    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisService.deleteHash(key);
    }
}
