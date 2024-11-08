package ojosama.talkak.recommendation.repository;

import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.EventQueue;
import ojosama.talkak.recommendation.key.EventQueueKey;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventQueueRepository {

    private final RedisUtil redisUtil;
    private final HashConverter hashConverter;


    public void save(EventQueue dto) {
        String key = EventQueueKey.QUEUE.getKey();
        redisUtil.setLValues(key, hashConverter.toMap(dto));
    }

}
