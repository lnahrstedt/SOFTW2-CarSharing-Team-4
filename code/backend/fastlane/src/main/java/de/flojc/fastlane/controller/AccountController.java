package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.request.AccountRequest;
import de.flojc.fastlane.response.UpdateAccountResponse;
import de.flojc.fastlane.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AccountController class is a REST controller that handles requests related to user accounts, including retrieving,
 * updating, and deleting accounts.
 */
@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * This function retrieves all accounts and returns them as a response entity.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping
    // Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> getAll() {
        log.info("Received request to get all accounts");
        List<Account> accounts = accountService.findAll();
        log.info("Fetched {} accounts", accounts.size());
        return ResponseEntity.ok(accounts);
    }

    /**
     * The function retrieves an account by its ID and returns it in a ResponseEntity, handling any exceptions that may
     * occur.
     *
     * @param id The "id" parameter is a path variable that is used to specify the ID of the account to retrieve. It is
     * extracted from the URL path and passed to the method as a Long value.
     * @return The method is returning a ResponseEntity object. If the account is found, the ResponseEntity will have a
     * status of 200 (OK) and the account object will be set as the response body. If an exception occurs, the
     * ResponseEntity will have the appropriate error status and the exception object will be set as the response body.
     */
    @GetMapping("/id/{id}")
    // Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #id == authentication.principal.id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received request to get account with id: {}", id);
        try {
            Account account = accountService.findById(id);
            log.info("Fetched account with id: {}", id);
            return ResponseEntity.ok(account);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The above function is a GET request handler that retrieves an account by email and returns it as a response entity.
     *
     * @param email The `email` parameter is a path variable that represents the email address of the account to be
     * retrieved.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/email/{email}")
    // Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        log.info("Received request to get account with email: {}", email);
        try {
            Account account = accountService.findByEmail(email);
            log.info("Fetched account with id: {}", email);
            return ResponseEntity.ok(account);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The function checks if an email exists in the system and returns an appropriate response.
     *
     * @param email The "email" parameter is a path variable that is passed in the URL. It represents the email address
     * that needs to be checked for existence.
     * @return The method doesEmailExist() returns a ResponseEntity object.
     */
    @GetMapping("/exist/{email}")
    //Tested
    public ResponseEntity<?> doesEmailExist(@PathVariable String email) {
        log.info("Received request to check if email {} exists", email);
        try {
            accountService.existByEmail(email);
            log.info("Email does not exist");
            return ResponseEntity.ok().build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function updates an account with the specified email and ID, using the provided account request object, and
     * returns the updated account response.
     *
     * @param email The email parameter is a path variable that represents the email of the account to be updated. It is
     * used to identify the specific account that needs to be updated.
     * @param id The "id" parameter is a Long value that represents the unique identifier of an account. It is used to
     * identify the specific account that needs to be updated.
     * @param accountWithUpdatedFields The parameter "accountWithUpdatedFields" is of type AccountRequest. It is used to
     * pass the updated fields of an account to the update method.
     * @return The method is returning a ResponseEntity object. If the update is successful, it returns a ResponseEntity
     * with HTTP status code 200 (OK) and the updated account response as the body. If there is an exception
     * (BackendException), it returns a ResponseEntity with the corresponding HTTP status code and the exception object as
     * the body.
     */
    @PatchMapping("/{email}/{id}")
    // Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #id == authentication.principal.id")
    public ResponseEntity<?> update(@PathVariable String email, @PathVariable Long id, @RequestBody AccountRequest accountWithUpdatedFields) {
        log.info("Received request to update account with email: {}", email);
        try {
            UpdateAccountResponse updateAccountResponse = accountService.update(email, id, accountWithUpdatedFields, true);
            log.info("Updated account with email: {}", email);
            return ResponseEntity.ok(updateAccountResponse);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function deletes an account with the specified ID, and returns a response indicating success or failure.
     *
     * @param id The "id" parameter in the above code represents the unique identifier of an account that needs to be
     * deleted.
     * @return The method is returning a ResponseEntity. If the account is successfully deleted, it returns a
     * ResponseEntity with no content and a status code of 204 (no content). If there is an exception (BackendException),
     * it returns a ResponseEntity with the exception as the body and the corresponding HTTP status code based on the error
     * code of the exception.
     */
    @DeleteMapping("/{id}")
    // Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #id == authentication.principal.id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Received request to markReservationAsCanceled account with id: {}", id);
        try {
            accountService.delete(id);
            log.info("Deleted account with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}