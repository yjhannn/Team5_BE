package ojosama.talkak.redis;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;

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

    public void setLValues(String key, Map<String, Object> data) {
        ListOperations<String, Object> values = redisTemplate.opsForList();
        values.rightPush(key, data);
    }

    public void setZValues(String key, String setKey, Float data) {
        ZSetOperations<String, Object> values = redisTemplate.opsForZSet();
        values.add(key, setKey, data);
    }

    public void setHashOps(String key, Map<String, Object> data) {
        redisTemplate.opsForHash().putAll(key, data);
    }

    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Optional<String> getValues(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value)
            .map(Object::toString);
    }

    public Map<String, Object> getHashOps(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.entrySet().stream()
            .collect(Collectors.toMap(
                e -> String.valueOf(e.getKey()),
                Map.Entry::getValue,
                (prev, next) -> next,
                HashMap::new
            ));
    }

    public Optional<String> getHashOps(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return Optional.ofNullable(value)
            .map(Object::toString);
    }

    public Set<TypedTuple<Object>> getSortedSetOps(String key, long cnt) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, cnt - 1);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void expireValues(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void deleteHashOps(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public void deleteHash(String key) {
        redisTemplate.delete(key);
    }

    public boolean checkExistValue(String value) {
        return !value.isEmpty();
    }


}
