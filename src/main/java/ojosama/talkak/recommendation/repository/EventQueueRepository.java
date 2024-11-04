package ojosama.talkak.recommendation.repository;

import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.RedisRepository;
import ojosama.talkak.recommendation.domain.EventQueue;
import ojosama.talkak.recommendation.key.EventQueueKey;
import org.springframework.stereotype.Repository;

@Repository
public class EventQueueRepository {

    private final RedisRepository redisRepository;
    private final HashConverter hashConverter;

    public EventQueueRepository(RedisRepository redisRepository, HashConverter hashConverter) {
        this.redisRepository = redisRepository;
        this.hashConverter = hashConverter;
    }

    public void save(EventQueue dto) {
        String key = EventQueueKey.QUEUE.getKey();
        redisRepository.setLValues(key, hashConverter.toMap(dto));
    }

}
