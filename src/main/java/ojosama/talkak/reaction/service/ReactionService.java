package ojosama.talkak.reaction.service;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.ReactionError;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.recommendation.domain.Reaction;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.innerkey.VideoHashKey;
import ojosama.talkak.recommendation.key.VideoKey;
import ojosama.talkak.recommendation.repository.ReactionsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionsRepository reactionsRepository;
    private final RedisUtil redisUtil;
    private final HashConverter hashConverter;


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
        redisUtil.incrementHashValue(key, VideoHashKey.VIEW_COUNT.getKey(), 1);
        return hashConverter.FromMap(redisUtil.getHashOps(key), VideoInfo.class);
    }

    public VideoInfo incrementLikeCount(Long categoryId, Long videoId) {
        String key = VideoKey.VIDEO_INFO.generateKey(categoryId, videoId);
        redisUtil.incrementHashValue(key, VideoHashKey.LIKE_COUNT.getKey(), 1);
        return hashConverter.FromMap(redisUtil.getHashOps(key), VideoInfo.class);
    }

}
