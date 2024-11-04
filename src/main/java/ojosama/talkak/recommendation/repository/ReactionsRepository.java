package ojosama.talkak.recommendation.repository;

import java.util.Map;
import java.util.Optional;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.RedisRepository;
import ojosama.talkak.recommendation.domain.Reaction;
import ojosama.talkak.recommendation.key.ReactionKey;
import org.springframework.stereotype.Repository;

@Repository
public class ReactionsRepository {

    private final RedisRepository redisRepository;
    private final HashConverter hashConverter;

    public ReactionsRepository(RedisRepository redisRepository, HashConverter hashConverter) {
        this.redisRepository = redisRepository;
        this.hashConverter = hashConverter;
    }

    public Reaction save(Long memberId, Long videoId, Reaction reaction) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisRepository.setHashOps(key, hashConverter.toMap(reaction));
        return hashConverter.FromMap(redisRepository.getHashOps(key), Reaction.class);
    }

    public Optional<Reaction> findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        Map<String, Object> entries = redisRepository.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, Reaction.class));
    }


    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisRepository.deleteHash(key);
    }
}
