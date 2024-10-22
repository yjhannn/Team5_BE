package ojosama.talkak.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import ojosama.talkak.redis.innerkey.VideoHashKey;
import ojosama.talkak.redis.key.VideoKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = {RedisConfig.class,
    RedisProperties.class,
    RedisService.class})
class RedisServiceTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
            .getConnection()
            .serverCommands()
            .flushAll();
    }

    @DisplayName("Redis 간단한 읽기 테스트")
    @Test
    void read() {
        Optional<String> value = redisService.getHashOps("video_1:1", "view_count");
        assertNotNull(value);
    }

    @DisplayName("Redis 간단한 쓰기 테스트")
    @Test
    void write() {
        redisService.setValues("demo", "demo_data");
        Optional<String> value = redisService.getValues("demo");
        assertNotNull(value);
        assertThat(value.get()).isEqualTo("demo_data");
    }

    @DisplayName("Key 변환 읽기 테스트")
    @Test
    void readKeyConverting() {
        Long categoryId = 1L;
        Long videoId = 1L;
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        String hashKey = VideoHashKey.CREATED_AT.getKey();
        Optional<String> value = redisService.getHashOps(key, hashKey);
        assertNotNull(value);
    }



}