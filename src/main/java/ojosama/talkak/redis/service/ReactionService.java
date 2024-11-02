package ojosama.talkak.redis.service;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.ReactionError;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.Reaction;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.redis.innerkey.VideoHashKey;
import ojosama.talkak.redis.key.VideoKey;
import ojosama.talkak.redis.repository.ReactionsRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {

    private final ReactionsRepository reactionsRepository;
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashConverter hashConverter;

    public ReactionService(ReactionsRepository reactionsRepository, RedisService redisService,
        RedisTemplate<String, Object> redisTemplate,
        HashConverter hashConverter) {
        this.reactionsRepository = reactionsRepository;
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
        this.hashConverter = hashConverter;
    }

    // 영상을 시청함과 동시에 Reactions 생성
    public Reaction createReaction(Long memberId, Long videoId) {
        return reactionsRepository.save(memberId, videoId, Reaction.createReaction());
    }

    public void toggleLike(Long memberId, Long videoId) {
        reactionsRepository.findByMemberIdAndVideoId(memberId, videoId)
            .ifPresentOrElse(reaction -> {
                    reaction.updateLike();
                    reactionsRepository.save(memberId, videoId, reaction);
                }, ()
                    -> {
                    createReaction(memberId, videoId);
                    throw TalKakException.of(ReactionError.FAILED_PROCESS_REQUEST);
                }
            );
    }

    public VideoInfo incrementViewCount(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisTemplate.opsForHash().increment(key, VideoHashKey.VIEW_COUNT.getKey(), 1);
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

    public VideoInfo incrementLikeCount(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisTemplate.opsForHash().increment(key, VideoHashKey.LIKE_COUNT.getKey(), 1);
        return hashConverter.FromMap(redisService.getHashOps(key), VideoInfo.class);
    }

}
