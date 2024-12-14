package ozdemir0ozdemir.nirobank.client.tokenclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.GenerateToken;


@FeignClient(url = "http://"+"${token-service:localhost:8081}"+"/api/v1/tokens", name = "tokens")
public interface TokenClient {

    @PostMapping
    ResponseEntity<Response<AccessToken>> generateToken(@RequestBody GenerateToken request);

    @GetMapping
    ResponseEntity<PagedResponse<RefreshToken>> findTokens(@RequestParam(name = "username", required = false) String username,
                                                           @RequestParam(name = "token-status", required = false) RefreshTokenStatus refreshTokenStatus,
                                                           @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                                           @RequestParam(name = "page-size", defaultValue = "10") int pageSize);

    @PostMapping("/{refreshTokenReferenceId}/refresh")
    ResponseEntity<Response<AccessToken>> refreshAccessToken(@PathVariable String refreshTokenReferenceId);

    @GetMapping("/{refreshTokenReferenceId}")
    ResponseEntity<Response<RefreshToken>> findRefreshTokenByReferenceId(@PathVariable String refreshTokenReferenceId);

}