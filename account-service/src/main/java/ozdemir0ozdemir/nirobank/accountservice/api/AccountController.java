package ozdemir0ozdemir.nirobank.accountservice.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.nirobank.accountservice.domain.Account;
import ozdemir0ozdemir.nirobank.accountservice.domain.Accounts;
import ozdemir0ozdemir.nirobank.accountservice.request.CreateAccount;
import ozdemir0ozdemir.nirobank.accountservice.request.SearchType;
import ozdemir0ozdemir.nirobank.common.response.PagedResponse;
import ozdemir0ozdemir.nirobank.common.response.Response;
import ozdemir0ozdemir.nirobank.common.response.ResponseStatus;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/accounts")
record AccountController(Accounts accounts) {

    @PostMapping
    ResponseEntity<Response<Void>> createAccount(@RequestBody CreateAccount request) {
        Long accountId = accounts.createAccount(request.username());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(accountId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(Response.succeeded("Account successfully created"));
    }

    @GetMapping("/{accountId}")
    ResponseEntity<Response<Account>> findAccountByAccountId(@PathVariable Long accountId) {
        Account account = accounts.findAccountByAccountId(accountId);

        return ResponseEntity
                .ok(Response.succeeded(account, "Account found"));
    }

    @GetMapping
    ResponseEntity<PagedResponse<Account>> searchAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "type", required = false) SearchType searchType,
            @RequestParam(name = "q", required = false) String query) {

        Page<Account> accountPage = accounts.findAllAccounts(page, size);
        PagedResponse<Account> response =
                PagedResponse.of(accountPage, ResponseStatus.SUCCEEDED, "Accounts page fetched");

        return ResponseEntity.ok(response);
    }

}
