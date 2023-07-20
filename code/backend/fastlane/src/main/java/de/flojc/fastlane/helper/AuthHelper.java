package de.flojc.fastlane.helper;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.model.Driver;
import de.flojc.fastlane.repository.AccountRepository;
import de.flojc.fastlane.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * The AuthHelper class provides methods for validating authentication and retrieving account and driver information.
 */
@RequiredArgsConstructor
public class AuthHelper {

    /**
     * The function validates authentication and retrieves an account by ID, throwing an exception if the account is not
     * found or if the authentication fails.
     *
     * @param givenAccountId The givenAccountId is the ID of the account that needs to be validated and retrieved.
     * @param email The "email" parameter is a String that represents the email address associated with the account.
     * @param accountRepository The accountRepository parameter is an instance of the AccountRepository class. It is used
     * to interact with the database and perform operations related to the Account entity, such as finding an account by
     * its ID or email.
     * @return The method is returning an Account object.
     */
    public static Account validateAuthAndGetAccountById(Long givenAccountId, String email, AccountRepository accountRepository) throws BackendException {
        Account acc = accountRepository.findById(givenAccountId).orElseThrow(() -> new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, givenAccountId));
        Account acc2 = accountRepository.findByEmail(email).orElseThrow(() -> new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, givenAccountId));
        if (acc != acc2) {
            throw new AccessDeniedException("");
        }
        return validateAuthAndGet(
                account -> account.getUser().getId(),
                accountRepository::findById,
                ErrorCode.ACCOUNT_NOT_FOUND,
                acc.getUser().getId()
        );
    }

    /**
     * The function validates authentication and retrieves a driver by user ID from a driver repository.
     *
     * @param givenUserId The givenUserId is the ID of the user for whom we want to find the driver.
     * @param driverRepository The driverRepository is an instance of the DriverRepository class, which is responsible for
     * accessing and manipulating driver data in the backend. It provides methods for querying and updating driver
     * information in the database.
     * @return The method is returning a Driver object.
     */
    public static Driver validateAuthAndGetDriverByUserId(Long givenUserId, DriverRepository driverRepository) throws BackendException {
        return validateAuthAndGet(
                account -> account.getUser().getId(),
                driverRepository::findByUserId,
                ErrorCode.DRIVER_NOT_FOUND,
                givenUserId);
    }

    /**
     * The function validates the authentication of a driver and retrieves the driver by their ID from a driver repository.
     *
     * @param givenDriverId The givenDriverId is the ID of the driver that needs to be validated and retrieved from the
     * driver repository.
     * @param driverRepository The `driverRepository` is an instance of a repository class that is responsible for
     * accessing and manipulating driver data in a database or any other data source. It provides methods to perform CRUD
     * (Create, Read, Update, Delete) operations on the driver entities. In this case, it is used to find
     * @return The method is returning a Driver object.
     */
    public static Driver validateAuthAndGetDriverById(Long givenDriverId, DriverRepository driverRepository) throws BackendException {
        Driver driver = driverRepository.findById(givenDriverId).orElseThrow(() -> new BackendException(ErrorCode.DRIVER_NOT_FOUND, givenDriverId));

        return validateAuthAndGet(
                account -> account.getUser().getId(),
                optionalDriver -> Optional.ofNullable(driver),
                ErrorCode.DRIVER_NOT_FOUND,
                driver.getUser().getId());
    }

    /**
     * The function validates authentication and retrieves an object based on a given ID, throwing exceptions if necessary.
     *
     * @param authExtractor A function that takes an Account object as input and returns a Long value representing the user
     * ID associated with that account. This function is used to extract the user ID of the logged-in account.
     * @param finder A function that takes a Long parameter and returns an Optional<T>. It is used to find an object of
     * type T based on the given ID.
     * @param errorCode The `errorCode` parameter is an instance of the `ErrorCode` enum. It represents the specific error
     * code that will be used when throwing a `BackendException` if the given ID is not found.
     * @param givenId The givenId parameter is the ID that is being validated and used to retrieve an object from the
     * backend.
     * @return The method is returning an object of type T.
     */
    private static <T> T validateAuthAndGet(Function<Account, Long> authExtractor, Function<Long, Optional<T>> finder, ErrorCode errorCode, Long givenId) throws BackendException {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (isMemberAccount(account)) {
            Long userIdOfLoggedInAccount = authExtractor.apply(account);
            if (!Objects.equals(userIdOfLoggedInAccount, givenId)) {
                throw new AccessDeniedException("");
            }
        }
        return finder.apply(givenId).orElseThrow(() -> new BackendException(errorCode, givenId));
    }

    /**
     * The function checks if an account's account type is "MEMBER".
     *
     * @param account An object of the class Account.
     * @return The method is returning a boolean value.
     */
    private static boolean isMemberAccount(Account account) {
        return Objects.equals(account.getAccountType().getName(), "MEMBER");
    }
}
