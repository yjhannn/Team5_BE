package ojosama.talkak.redis.repository;

import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.redis.innerkey.VideoHashKey;
import ojosama.talkak.redis.key.VideoKey;
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

    public VideoInfo findByCategoryAndVideoId(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

    public VideoInfo updateViewCount(Long categoryId, Long videoId, Long updatedViewCount) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisService.setHashValue(key, VideoHashKey.VIEW_COUNT.getKey(), updatedViewCount);
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

    public VideoInfo updateLikeCount(Long categoryId, Long videoId, Long updatedLikeCount) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisService.setHashValue(key, VideoHashKey.LIKE_COUNT.getKey(), updatedLikeCount);
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

    public void delete(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisService.deleteHash(key);
    }
}
