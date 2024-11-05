package ojosama.talkak.redis.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.config.RedisConfig;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.EventQueue;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.innerkey.ScoresSetKey;
import ojosama.talkak.recommendation.key.ScoresKey;
import ojosama.talkak.recommendation.repository.EventQueueRepository;
import ojosama.talkak.recommendation.repository.ScoresRepository;
import ojosama.talkak.recommendation.repository.VideoInfoRepository;
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
    RedisProperties.class, RedisUtil.class, VideoInfoRepository.class,
    ScoresRepository.class, EventQueueRepository.class})
class RedisUtilTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private HashConverter hashConverter;
    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private ScoresRepository scoresRepository;
    @Autowired
    private EventQueueRepository eventQueueRepository;

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
        VideoInfo dto = VideoInfo.of(LocalDateTime.now(), 100L, 100L);
        VideoInfo responseDto = videoInfoRepository.save(1L, 1L, dto);

        assertThat(responseDto.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(responseDto.getLikeCount()).isEqualTo(dto.getLikeCount());
        assertThat(responseDto.getViewCount()).isEqualTo(dto.getViewCount());
    }

    @DisplayName("Redis Sorted Set 읽기/쓰기 테스트")
    @Test
    void sortedSetTest() {
        String key = ScoresKey.SCORES.generateKey(1L, 1L);
        for (int i = 0; i < 100; i++) {
            Float scores = 0f + i + 1;
            String setKey = ScoresSetKey.SCORE.generateKey((long) i + 1);
            redisUtil.setZValues(key, setKey, scores);
        }

        Set<TypedTuple<Object>> value = redisUtil.getSortedSetOps(key, 10);
        assertThat(value).hasSize(10);
        assertThat(value)
            .satisfies(set -> {
                List<TypedTuple<Object>> list = new ArrayList<>(set);
                IntStream.range(0, 10)
                    .forEach(i -> {
                        ZSetOperations.TypedTuple<Object> tuple = list.get(i);
                        String id = (String) tuple.getValue();
                        Double score = tuple.getScore();

                        assertThat(id).isEqualTo(ScoresSetKey.SCORE.generateKey((long) (100 - i)));
                        assertThat(score).isEqualTo(100 - i);
                    });
            });
    }

    @DisplayName("Redis List 쓰기 테스트")
    @Test
    void listTest() {
        Long memberId = 1L;
        Long categoryId = 1L;
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        EventQueue dto = EventQueue.of(memberId, categoryId, lastUpdatedAt);
        eventQueueRepository.save(dto);
        // 이벤트 큐에 읽기 작업을 수행하지는 않으므로, List 읽기는 구현하지 않았으므로
        // Redis 저장소에 잘 저장되었는지 직접 확인해야 한다.
    }


}