package ozdemir0ozdemir.nirobank.client.tokenclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.CreateToken;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.RefreshToken;


@FeignClient(url = "http://"+"${token-service:localhost:8081}"+"/api/v1/tokens", name = "tokens")
public interface TokenClient {

    @PostMapping
    Response<AccessToken> createToken(@RequestBody CreateToken request);

    @PostMapping("/refresh")
    Response<AccessToken> refreshAccessToken(@RequestBody RefreshToken request);

    @GetMapping
    PagedResponse<Token> findTokens(@RequestParam(name = "username", required = false) String username,
                                    @RequestParam(name = "token-status", required = false) TokenStatus tokenStatus,
                                    @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                    @RequestParam(name = "page-size", defaultValue = "10") int pageSize);

    @GetMapping("/{tokenId}")
    Response<Token> getTokenByTokenId(@PathVariable String tokenId);

    @PostMapping("/{tokenId}")
    void revokeTokenByTokenId(@PathVariable String tokenId);

    @DeleteMapping("/{tokenId}")
    void deleteTokenByTokenId(@PathVariable String tokenId);

}