package ojosama.talkak.recommendation.repository;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.key.VideoKey;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VideoInfoRepository {

    private final RedisUtil redisUtil;
    private final HashConverter hashConverter;

    public VideoInfo save(Long categoryId, Long videoId, VideoInfo videoInfo) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisUtil.setHashOps(key, hashConverter.toMap(videoInfo));
        return hashConverter.FromMap(redisUtil.getHashOps(key), VideoInfo.class);
    }

    public Optional<VideoInfo> findByCategoryAndVideoId(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        Map<String, Object> entries = redisUtil.getHashOps(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(hashConverter.FromMap(entries, VideoInfo.class));
    }

    public void delete(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisUtil.deleteHash(key);
    }
}
