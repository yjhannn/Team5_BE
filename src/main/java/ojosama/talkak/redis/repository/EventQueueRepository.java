package ojosama.talkak.redis.repository;

import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.EventQueue;
import ojosama.talkak.redis.key.EventQueueKey;
import org.springframework.stereotype.Repository;

@Repository
public class EventQueueRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public EventQueueRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public void save(EventQueue dto) {
        String key = EventQueueKey.QUEUE.getKey();
        redisService.setLValues(key, hashConverter.toMap(dto));
    }

}
