package ojosama.talkak.redis.repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.UserMeta;
import ojosama.talkak.redis.innerkey.UserMetaHashKey;
import ojosama.talkak.redis.key.UserMetaKey;
import org.springframework.stereotype.Repository;

@Repository
public class UserMetaRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public UserMetaRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public UserMeta save(Long memberId, UserMeta userMeta) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.setHashOps(key, hashConverter.toMap(userMeta));
        return hashConverter.FromMap(redisService.getHashOps(key), UserMeta.class);
    }

    public Optional<UserMeta> findByMemberId(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        Map<String, Object> entries = redisService.getHashOps(key);

        if(entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, UserMeta.class));
    }

    public UserMeta updateLastUpdatedAt(Long memberId, LocalDateTime dateTime) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.setHashValue(key, UserMetaHashKey.LAST_UPDATED_AT.getKey(), HashConverter.fromDateTime(dateTime));
        return hashConverter.FromMap(redisService.getHashOps(key), UserMeta.class);
    }

    public void delete(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.deleteHash(key);
    }

}
