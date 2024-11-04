package ojosama.talkak.redis.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import ojosama.talkak.recommendation.repository.VideoInfoRepository;
import ojosama.talkak.redis.config.RecommendTestContainerConfig;
import ojosama.talkak.redis.config.RedisTestContainerConfig;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.reaction.service.ReactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
@ExtendWith({RedisTestContainerConfig.class, RecommendTestContainerConfig.class})
class VideoInfoConcurrencyTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private VideoInfoRepository videoInfoRepository;

    private VideoInfo videoInfo;
    private Long categoryId = 1L;
    private Long videoId = 1L;

    @BeforeEach
    void setUp() {
        videoInfo = videoInfoRepository.save(categoryId, videoId, VideoInfo.of(LocalDateTime.now(), 0L, 0L));
    }

    @DisplayName("조회수 동시성 테스트")
    @Test
    void views_concurrency_test() throws InterruptedException {
        int threadCount = 1243;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                reactionService.incrementViewCount(categoryId, videoId);
                latch.countDown();
            });
        }

        latch.await();
        Optional<VideoInfo> updatedViewInfo = videoInfoRepository.findByCategoryAndVideoId(categoryId,
            videoId);
        Assertions.assertThat(updatedViewInfo.get().getViewCount()).isEqualTo(Long.valueOf(threadCount));
    }

    @DisplayName("좋아요수 동시성 테스트")
    @Test
    void likes_concurrency_test() throws InterruptedException {
        int threadCount = 1496;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                reactionService.incrementLikeCount(categoryId, videoId);
                latch.countDown();
            });
        }

        latch.await();
        Optional<VideoInfo> updatedViewInfo = videoInfoRepository.findByCategoryAndVideoId(categoryId,
            videoId);
        Assertions.assertThat(updatedViewInfo.get().getLikeCount()).isEqualTo(Long.valueOf(threadCount));
    }

}