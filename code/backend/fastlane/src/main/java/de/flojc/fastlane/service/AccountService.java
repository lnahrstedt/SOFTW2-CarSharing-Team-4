package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.helper.AuthHelper;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.model.AccountType;
import de.flojc.fastlane.model.Reservation;
import de.flojc.fastlane.repository.AccountRepository;
import de.flojc.fastlane.request.AccountRequest;
import de.flojc.fastlane.response.UpdateAccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * The `AccountService` class is a service that handles operations related to user accounts, such as retrieving, saving,
 * updating, and deleting accounts.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final AccountTypeService accountTypeService;
    private final DriverService driverService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final TokenService tokenService;


    private final PasswordEncoder passwordEncoder;

    /**
     * This function retrieves all accounts from the account repository and logs the number of accounts found.
     *
     * @return The method is returning a List of Account objects.
     */
    public List<Account> findAll() {
        log.debug("Starting retrieval of all accounts...");
        List<Account> accounts = accountRepository.findAll();
        log.debug("Finished retrieval of all accounts - {} accounts found.", accounts.size());
        return accounts;
    }

    /**
     * The function retrieves an account by its ID and throws a BackendException if the account is not found.
     *
     * @param id The "id" parameter is a long value representing the unique identifier of the account that needs to be
     * retrieved.
     * @return The method is returning an Account object.
     */
    public Account findById(long id) throws BackendException {
        log.debug("Starting retrieval of account with id: {}...", id);
        Account account = accountRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, id));
        log.debug("Finished retrieval of account with id: {}", id);
        return account;
    }

    /**
     * The function findByEmail retrieves an Account object from the account repository based on the provided email.
     *
     * @param email The parameter "email" is a String that represents the email address of the account that we want to
     * find.
     * @return The method is returning an Account object.
     */
    public Account findByEmail(String email) throws BackendException {
        log.debug("Starting retrieval of account with email: {}...", email);
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, email));
        log.debug("Finished retrieval of account with email: {}", email);
        return account;
    }

    /**
     * The function saves an account to the repository and returns the saved account.
     *
     * @param account The "account" parameter is an instance of the Account class that represents the account to be saved
     * in the repository.
     * @return The method is returning the saved account object.
     */
    public Account save(Account account) throws BackendException {
        log.debug("Starting to save an account to the repository...");
        Account savedAccount = accountRepository.save(account);
        log.debug("Finished saving an account to the repository. Id of saved account: {}", account.getId());
        return savedAccount;
    }

    /**
     * The function finds or saves an account and returns the result.
     *
     * @param account The "account" parameter is an instance of the Account class. It represents the account that needs to
     * be found or saved in the system.
     * @return The method is returning an instance of the Account class.
     */
    public Account findOrSave(Account account) {
        log.debug("Starting findOrSave operation for account: {}...", account);
        Account result = HelperService.findOrSave(account, accountRepository);
        log.debug("Completed findOrSave operation for account: {}", account);
        return result;
    }

    /**
     * The `update` function updates an account with the provided email and ID, using the fields from the `AccountRequest`
     * object, and returns an `UpdateAccountResponse` object.
     *
     * @param email The email parameter is a String that represents the email of the account to be updated.
     * @param id The `id` parameter is the unique identifier of the account that needs to be updated.
     * @param accountWithUpdatedFields An object of type AccountRequest that contains the updated fields for the account.
     * @param validateAuth The `validateAuth` parameter is a boolean flag that determines whether the authentication and
     * authorization should be validated before updating the account. If `validateAuth` is set to `true`, the method will
     * validate the authentication and authorization of the user before updating the account. If `validateAuth` is set to
     * @return The method returns an object of type UpdateAccountResponse.
     */
    public UpdateAccountResponse update(String email, long id, AccountRequest accountWithUpdatedFields, boolean validateAuth) throws BackendException {
        log.debug("Started update of account with email: {}...", email);
        accountWithUpdatedFields.validateFieldsNotBlank();

        Account accountOfLoggedInPerson = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account accountToBeUpdated;
        if (validateAuth) {
            accountToBeUpdated = AuthHelper.validateAuthAndGetAccountById(id, email, accountRepository);
        } else {
            accountToBeUpdated = findById(id);
        }

        if (accountWithUpdatedFields.getEmail() != null) {
            accountToBeUpdated.setEmail(accountWithUpdatedFields.getEmail());
        }

        if (accountWithUpdatedFields.getPassword() != null) {
            accountToBeUpdated.setPassword(passwordEncoder.encode(accountWithUpdatedFields.getPassword()));
        }

        if (accountWithUpdatedFields.getPhone() != null) {
            accountToBeUpdated.setPhone(accountWithUpdatedFields.getPhone());
        }

        if (accountWithUpdatedFields.getAccountType() != null) {
            String accountTypeName = accountWithUpdatedFields.getAccountType();
            AccountType accountType = accountTypeService.getAccountTypeByName(accountTypeName);
            accountToBeUpdated.setAccountType(accountType);
        }

        Account savedAccount = accountRepository.save(accountToBeUpdated);

        if (Objects.equals(savedAccount.getId(), accountOfLoggedInPerson.getId())) {
            log.debug("Finished update of account with email: {}", email);
            return tokenService.generateAuthenticationResponse(accountToBeUpdated);
        } else {
            log.debug("Finished update of account with email: {}", email);
            return savedAccount;
        }
    }

    /**
     * The function deletes an account with a given ID and its associated nested objects, and throws a BackendException if
     * the account is not found.
     *
     * @param id The "id" parameter represents the unique identifier of the account that needs to be deleted.
     */
    public void delete(long id) throws BackendException {
        log.debug("Starting deletion of account with id: {}...", id);
        try {
            deleteAccountAndNestedObjects(id);
        } catch (BackendException e) {
            ErrorCode errorCode = ErrorCode.ACCOUNT_NOT_FOUND;
            e.setErrorCode(errorCode);
            e.setDescription(String.format(errorCode.getDescription(), id));
            throw e;
        }
        log.debug("Finished deletion of account with id: {}", id);
    }

    /**
     * The function deletes an account and its nested objects, such as reservations and employees, based on the account
     * type.
     *
     * @param id The ID of the account that needs to be deleted.
     */
    private void deleteAccountAndNestedObjects(long id) throws BackendException {
        Account account = findById(id);
        String accountTypeName = account.getAccountType().getName();
        long userId = account.getUser().getId();
        List<Account> accounts = accountRepository.findAccountsByUserId(account.getUser().getId());
        if (accountTypeName.equals("MEMBER") || accountTypeName.equals("MEMBER_EMPLOYEE")) {
            long driverId = driverService.findByUserId(userId, false).getId();
            deleteIfAllReservationsPaid(driverId);
        } else {
            String employeeId = employeeService.findByUserId(userId).getId();
            employeeService.delete(employeeId);
        }
        if (accounts.size() > 1) {
            userService.delete(userId);
        }
        HelperService.delete(id, accountRepository);
    }

    /**
     * The function deletes a driver and all their reservations if all the reservations are either canceled or paid.
     *
     * @param driverId The driverId parameter is the unique identifier of the driver for whom we want to delete all
     * reservations if they are all paid.
     */
    private void deleteIfAllReservationsPaid(long driverId) throws BackendException {
        List<Reservation> reservations = reservationService.getReservationsByDriverId(driverId);
        for (Reservation reservation : reservations) {
            if (!(reservation.getReservationState().getName().equals("CANCELED") || reservation.getReservationState().getName().equals("PAID"))) {
                throw new BackendException(ErrorCode.RESERVATION_NOT_PAID, reservation.getId());
            }
        }
        reservationService.deleteAll(reservations, driverId);
        driverService.delete(driverId);
    }

    /**
     * The function checks if an account with a given email address already exists and throws an exception if it does.
     *
     * @param email The "email" parameter is a string that represents an email address.
     */
    public void existByEmail(String email) throws BackendException {
        log.debug("Checking existence of account with email address: {}...", email);
        boolean exists = accountRepository.existsByEmail(email);
        log.debug("Existence of account with email: {} checked. Exists: {}...", email, exists);
        if (exists) {
            throw new BackendException(ErrorCode.EMAIL_ADDRESS_ALREADY_IN_USE, email);
        }
    }

    /**
     * The function finds accounts by user ID and logs the start and completion of the operation.
     *
     * @param userId The userId parameter is a Long value that represents the unique identifier of a user.
     * @return The method is returning a List of Account objects.
     */
    public List<Account> findAccountsByUserId(Long userId) {
        log.debug("Starting findAccountsByUserId operation for user id: {}...", userId);
        List<Account> result = accountRepository.findAccountsByUserId(userId);
        log.debug("Completed findAccountsByUserId operation for user id: {}. Found {} accounts.", userId, result.size());
        return result;
    }

    /**
     * The function loads a user's details by their username and throws an exception if the username is not found.
     *
     * @param username The username parameter is the email address of the user that is being searched for in the account
     * repository.
     * @return The method is returning a UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}