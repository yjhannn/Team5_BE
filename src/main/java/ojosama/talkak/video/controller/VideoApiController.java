 package ojosama.talkak.video.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import ojosama.talkak.common.exception.ErrorResponse;
import ojosama.talkak.video.request.VideoCategoryRequest;
import ojosama.talkak.video.request.YoutubeCategoryRequest;
import ojosama.talkak.video.request.YoutubeUrlValidationRequest;
import ojosama.talkak.video.response.VideoDetailsResponse;
import ojosama.talkak.video.response.VideoInfoResponse;
import ojosama.talkak.video.response.YoutubeApiResponse;
import ojosama.talkak.video.response.YoutubeUrlValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

 public interface VideoApiController {

    @Operation(summary = "유튜브 url 검증", description = "유튜브 url을 검증합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유튜브 url 조회 성공"),
        @ApiResponse(responseCode = "V001", description = "존재하지 않는 유튜브 url",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<YoutubeUrlValidationResponse> validateYoutubeUrl(@RequestBody YoutubeUrlValidationRequest req);

    @Operation(summary = "유튜브 쇼츠 불러오기", description = "유튜브 쇼츠 영상을 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유튜브 쇼츠 영상 불러오기 성공")
    })
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShorts() throws IOException, IOException;

    @Operation(summary = "카테고리별 유튜브 쇼츠 불러오기", description = "카테고리별 유튜브 쇼츠를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리별 유튜브 쇼츠 영상 불러오기 성공"),
        @ApiResponse(responseCode = "C001", description = "존재하지 않는 카테고리입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShortsByCategory(
        YoutubeCategoryRequest youtubeCategoryRequest) throws IOException;

    @Operation(summary = "특정 영상 불러오기", description = "특정 영상에 대한 자세한 정보 불러오기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 영상 정보 불러오기 성공"),
        @ApiResponse(responseCode = "M001", description = "존재하지 않는 회원"),
        @ApiResponse(responseCode = "V004", description = "존재하지 않는 비디오",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<VideoDetailsResponse> getVideoDetails(@PathVariable Long videoId);

    @Operation(summary = "카테고리별 영상 불러오기", description = "카테고리별 영상에 대한 리스트 불러오기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리별 영상 불러오기 성공"),
        @ApiResponse(responseCode = "V004", description = "존재하지 않는 비디오",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<VideoInfoResponse>> getPopularVideosByCategory(@RequestBody VideoCategoryRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size);
}
