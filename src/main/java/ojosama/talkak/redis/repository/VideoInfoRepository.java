package ojosama.talkak.redis.repository;

import java.util.Map;
import java.util.Optional;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.redis.innerkey.VideoHashKey;
import ojosama.talkak.redis.key.VideoKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoInfoRepository {

    private final RedisService redisService;
    private final HashConverter hashConverter;

    public VideoInfoRepository(RedisService redisService, HashConverter hashConverter) {
        this.redisService = redisService;
        this.hashConverter = hashConverter;
    }

    public VideoInfo save(Long categoryId, Long videoId, VideoInfo videoInfo) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisService.setHashOps(key, hashConverter.toMap(videoInfo));
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

    public Optional<VideoInfo> findByCategoryAndVideoId(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        Map<String, Object> entries = redisService.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, VideoInfo.class));
    }

    public void delete(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisService.deleteHash(key);
    }
}
