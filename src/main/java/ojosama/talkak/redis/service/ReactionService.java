package ojosama.talkak.redis.service;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.ReactionError;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.reaction.domain.Reaction;
import ojosama.talkak.reaction.domain.ReactionId;
import ojosama.talkak.reaction.repository.ReactionRepository;
import ojosama.talkak.redis.HashConverter;
import ojosama.talkak.redis.RedisService;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.redis.innerkey.VideoHashKey;
import ojosama.talkak.redis.key.VideoKey;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashConverter hashConverter;

    public ReactionService(ReactionRepository reactionRepository, MemberRepository memberRepository,
        VideoRepository videoRepository, RedisService redisService,
        RedisTemplate<String, Object> redisTemplate,
        HashConverter hashConverter) {
        this.reactionRepository = reactionRepository;
        this.memberRepository = memberRepository;
        this.videoRepository = videoRepository;
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
        this.hashConverter = hashConverter;
    }

    public void toggleLike(Long videoId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> TalKakException.of(ReactionError.INVALID_MEMBER_ID));
        Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> TalKakException.of(ReactionError.INVALID_VIDEO_ID));
        ReactionId reactionId = new ReactionId(memberId, videoId);
        reactionRepository.findById(reactionId)
            .ifPresentOrElse(
                existingReaction -> removeExistingReactionAndUpdateLikes(existingReaction, video),
                () -> {
                    Reaction newReaction = new Reaction(reactionId, member, video, true);
                    reactionRepository.save(newReaction);
                    video.incrementLikes();
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



    private void removeExistingReactionAndUpdateLikes(Reaction existingReaction, Video video) {
        reactionRepository.delete(existingReaction);
        video.decrementLikes();
    }
}