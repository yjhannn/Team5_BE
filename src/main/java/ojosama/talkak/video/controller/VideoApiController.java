package ojosama.talkak.video.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import ojosama.talkak.common.exception.ErrorResponse;
import ojosama.talkak.video.request.YoutubeCategoryRequest;
import ojosama.talkak.video.response.YoutubeApiResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "영상 정보 API", description = "영상 정보 관련 API를 담당합니다.")
public interface VideoApiController {
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인기 유튜브 영상 불러오기 성공")
    })
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShorts() throws IOException;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리별 유튜브 영상 불러오기 성공"),
        @ApiResponse(responseCode = "C001", description = "존재하지 않는 카테고리입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShortsByCategory(YoutubeCategoryRequest youtubeCategoryRequest) throws IOException;
}
