package ojosama.talkak.redis.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventQueueKey {
    QUEUE("event_queue");

    private final String key;
}
