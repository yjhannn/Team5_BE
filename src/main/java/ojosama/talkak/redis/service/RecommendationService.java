package ojosama.talkak.redis.service;

import java.util.List;
import ojosama.talkak.redis.domain.Scores;
import ojosama.talkak.redis.repository.ScoresRepository;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    private ScoresRepository scoresRepository;
    private VideoRepository videoRepository;

    public static final long RECOMMENDATION_VIDEOS_COUNT = 10;

    public RecommendationService(ScoresRepository scoresRepository, VideoRepository videoRepository) {
        this.scoresRepository = scoresRepository;
        this.videoRepository = videoRepository;
    }

    public List<Video> getDefaultRecommendationVideos(Long categoryId) {
        return videoRepository.findAllById(scoresRepository.findDefaultTopRank(categoryId, RECOMMENDATION_VIDEOS_COUNT)
            .stream().map(Scores::videoId)
            .toList());
    }

    public List<Video> getRecommendedVideos(Long memberId, Long categoryId) {
        List<Scores> topRankScores = scoresRepository.findTopRankScores(memberId, categoryId,
            RECOMMENDATION_VIDEOS_COUNT);

        if (topRankScores.isEmpty()) {
            return getDefaultRecommendationVideos(categoryId);
        }

        return videoRepository.findAllById(   topRankScores.stream()
            .map(Scores::videoId)
            .toList());
    }
}
