package ojosama.talkak.reaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ojosama.talkak.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "좋아요 API", description = "좋아요 관련 API를 담당합니다.")
public interface ReactionApiController {

    @Operation(summary = "좋아요 요청", description = "좋아요를 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "좋아요 요청 성공"),
        @ApiResponse(responseCode = "R001", description = "인증되지 않은 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "R003", description = "유효하지 않은 videoId", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "R004", description = "좋아요 요청 처리 실패, 다시 시도해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> toggleLike(@PathVariable Long videoId, Authentication authentication);
}
