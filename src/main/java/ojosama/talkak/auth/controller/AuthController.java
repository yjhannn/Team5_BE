package ojosama.talkak.auth.controller;

import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.dto.ReissueRequest;
import ojosama.talkak.auth.dto.TokenResponse;
import ojosama.talkak.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController implements AuthApiController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody ReissueRequest request,
        Authentication authentication)
    {
        Long id = Long.valueOf(authentication.getPrincipal().toString());
        TokenResponse tokenResponse = authService.reissue(request.refreshToken(), id);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/issue")
    public ResponseEntity<TokenResponse> issue() {
        return ResponseEntity.ok(authService.issue());
    }
}
