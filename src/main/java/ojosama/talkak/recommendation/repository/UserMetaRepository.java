package ojosama.talkak.recommendation.repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.UserMeta;
import ojosama.talkak.recommendation.innerkey.UserMetaHashKey;
import ojosama.talkak.recommendation.key.UserMetaKey;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMetaRepository {

    private final RedisUtil redisUtil;
    private final HashConverter hashConverter;

    public UserMeta save(Long memberId, UserMeta userMeta) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisUtil.setHashOps(key, hashConverter.toMap(userMeta));
        return hashConverter.FromMap(redisUtil.getHashOps(key), UserMeta.class);
    }

    public Optional<UserMeta> findByMemberId(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        Map<String, Object> entries = redisUtil.getHashOps(key);

        if(entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, UserMeta.class));
    }

    public UserMeta updateLastUpdatedAt(Long memberId, LocalDateTime dateTime) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisUtil.setHashValue(key, UserMetaHashKey.LAST_UPDATED_AT.getKey(), HashConverter.fromDateTime(dateTime));
        return hashConverter.FromMap(redisUtil.getHashOps(key), UserMeta.class);
    }

    public void delete(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisUtil.deleteHash(key);
    }

}
