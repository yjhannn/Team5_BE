package ojosama.talkak.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ojosama.talkak.auth.dto.ReissueRequest;
import ojosama.talkak.auth.dto.TokenResponse;
import ojosama.talkak.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Tag(name = "인증 API", description = "인증 관련 API를 담당합니다.")
public interface AuthApiController {

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "A001", description = "유효하지 않은 access token입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "A002", description = "유효하지 않은 refresh token입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<TokenResponse> reissue(ReissueRequest request, Authentication authentication);

    @Operation(summary = "토큰 발급", description = "테스트용 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공"),
    })
    ResponseEntity<TokenResponse> issue();
}
