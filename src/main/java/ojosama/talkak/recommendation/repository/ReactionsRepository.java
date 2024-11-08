package ojosama.talkak.recommendation.repository;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.Reaction;
import ojosama.talkak.recommendation.key.ReactionKey;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionsRepository {

    private final RedisUtil redisUtil;
    private final HashConverter hashConverter;

    public Reaction save(Long memberId, Long videoId, Reaction reaction) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisUtil.setHashOps(key, hashConverter.toMap(reaction));
        return hashConverter.FromMap(redisUtil.getHashOps(key), Reaction.class);
    }

    public Optional<Reaction> findByMemberIdAndVideoId(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        Map<String, Object> entries = redisUtil.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, Reaction.class));
    }


    public void delete(Long memberId, Long videoId) {
        String key = ReactionKey.REACTION.generateKey(memberId, videoId);
        redisUtil.deleteHash(key);
    }
}
