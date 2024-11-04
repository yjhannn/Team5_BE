package ojosama.talkak.recommendation.repository;

import java.util.Map;
import java.util.Optional;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.RedisRepository;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.key.VideoKey;
import org.springframework.stereotype.Repository;

@Repository
public class VideoInfoRepository {

    private final RedisRepository redisRepository;
    private final HashConverter hashConverter;

    public VideoInfoRepository(RedisRepository redisRepository, HashConverter hashConverter) {
        this.redisRepository = redisRepository;
        this.hashConverter = hashConverter;
    }

    public VideoInfo save(Long categoryId, Long videoId, VideoInfo videoInfo) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisRepository.setHashOps(key, hashConverter.toMap(videoInfo));
        return hashConverter.FromMap(redisRepository.getHashOps(key), VideoInfo.class);
    }

    public Optional<VideoInfo> findByCategoryAndVideoId(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        Map<String, Object> entries = redisRepository.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, VideoInfo.class));
    }

    public void delete(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisRepository.deleteHash(key);
    }
}
