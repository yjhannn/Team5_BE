package ojosama.talkak.redis;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public Optional<String> getValues(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value)
            .map(Object::toString);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void expireValues(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, String> data) {
        redisTemplate.opsForHash().putAll(key, data);
    }

    public Optional<String> getHashOps(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return Optional.ofNullable(value)
            .map(Object::toString);
    }

    public void deleteHashOps(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public boolean checkExistValue(String value) {
        return !value.isEmpty();
    }


}
