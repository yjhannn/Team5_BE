package ojosama.talkak.redis.repository;

import java.time.LocalDateTime;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.dto.UserMetaDto;
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

    public UserMetaDto save(Long memberId, UserMetaDto userMetaDto) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.setHashOps(key, hashConverter.toMap(userMetaDto));
        return hashConverter.FromMap(redisService.getHashOps(key), UserMetaDto.class);
    }

    public UserMetaDto findByMemberId(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        return hashConverter.FromMap(redisService.getHashOps(key), UserMetaDto.class);
    }

    public UserMetaDto updateLastUpdatedAt(Long memberId, LocalDateTime dateTime) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.setHashValue(key, UserMetaHashKey.LAST_UPDATED_AT.getKey(), HashConverter.fromDateTime(dateTime));
        return hashConverter.FromMap(redisService.getHashOps(key), UserMetaDto.class);
    }

    public void delete(Long memberId) {
        String key = UserMetaKey.USER_META.generateKey(memberId);
        redisService.deleteHash(key);
    }

}
