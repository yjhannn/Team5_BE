package ojosama.talkak.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import ojosama.talkak.redis.dto.EventQueueDto;
import ojosama.talkak.redis.dto.ScoresDto;
import ojosama.talkak.redis.dto.VideoInfoDto;
import ojosama.talkak.redis.innerkey.ScoresSetKey;
import ojosama.talkak.redis.key.EventQueueKey;
import ojosama.talkak.redis.key.ScoresKey;
import ojosama.talkak.redis.key.VideoKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

@SpringBootTest(classes = {RedisConfig.class,
    RedisProperties.class, RedisService.class})
class RedisServiceTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HashConverter hashConverter;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
            .getConnection()
            .serverCommands()
            .flushAll();
    }

    @DisplayName("Redis Hash 읽기/쓰기 테스트")
    @Test
    void hashTest() {
        String key = VideoKey.VIDEO_INFO.generateKey(1L, 1L);
        VideoInfoDto dto = VideoInfoDto.of(LocalDateTime.now(), 100L, 100L);
        redisService.setHashOps(key, hashConverter.toMap(dto));
        Map<String, Object> value = redisService.getHashOps(key);
        VideoInfoDto responseDto = hashConverter.FromMap(value, VideoInfoDto.class);

        assertThat(responseDto.createdAt()).isEqualTo(dto.createdAt());
        assertThat(responseDto.likeCount()).isEqualTo(dto.likeCount());
        assertThat(responseDto.viewCount()).isEqualTo(dto.viewCount());
    }

    @DisplayName("Redis Sorted Set 읽기/쓰기 테스트")
    @Test
    void sortedSetTest() {
        String key = ScoresKey.SCORES.generateKey(1L);
        for (int i = 0; i < 100; i++) {
            ScoresDto dto = ScoresDto.of(0f + i + 1);
            String setKey = ScoresSetKey.SCORE.generateKey(1L, (long) i + 1);
            redisService.setZValues(key, setKey, dto.score());
        }

        Set<TypedTuple<Object>> value = redisService.getSortedSetOps(key, 10);
        assertThat(value).hasSize(10);
        assertThat(value)
            .satisfies(set -> {
                List<TypedTuple<Object>> list = new ArrayList<>(set);
                IntStream.range(0, 10)
                    .forEach(i -> {
                        ZSetOperations.TypedTuple<Object> tuple = list.get(i);
                        String id = (String) tuple.getValue();
                        Double score = tuple.getScore();

                        assertThat(id).isEqualTo(ScoresSetKey.SCORE.generateKey(1L,
                            (long) (100 - i)));
                        assertThat(score).isEqualTo(100 - i);
                    });
            });
    }

    @DisplayName("Redis List 쓰기 테스트")
    @Test
    void listTest() {
        String key = EventQueueKey.QUEUE.getKey();
        Long memberId = 1L;
        Long categoryId = 1L;
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        EventQueueDto dto = EventQueueDto.of(memberId, categoryId, lastUpdatedAt);
        redisService.setLValues(key, hashConverter.toMap(dto));
        // 이벤트 큐에 읽기 작업을 수행하지는 않으므로, List 읽기는 구현하지 않았으므로
        // Redis 저장소에 잘 저장되었는지 직접 확인해야 한다.
    }


}