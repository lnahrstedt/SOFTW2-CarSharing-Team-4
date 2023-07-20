package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.AccountType;
import de.flojc.fastlane.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The AccountTypeService class provides methods for finding or saving an account type and retrieving an account type by
 * name.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    /**
     * The function finds or saves an account type and returns the result.
     *
     * @param accountType The account type object that needs to be found or saved.
     * @return The method is returning an object of type AccountType.
     */
    public AccountType findOrSave(AccountType accountType) {
        log.debug("Starting findOrSave operation for account type: {}...", accountType);
        AccountType result = HelperService.findOrSave(accountType, accountTypeRepository);
        log.debug("Completed findOrSave operation for account type: {}. Result: {}", accountType, result);
        return result;
    }

    /**
     * The function retrieves an AccountType object by its name and throws a BackendException if the account type is not
     * found.
     *
     * @param accountTypeName The accountTypeName parameter is a String that represents the name of the account type that
     * we want to retrieve.
     * @return The method is returning an object of type AccountType.
     */
    public AccountType getAccountTypeByName(String accountTypeName) throws BackendException {
        log.debug("Starting getAccountTypeByName operation for account type name: {}...", accountTypeName);
        AccountType result = accountTypeRepository.getAccountTypeByNameIgnoreCase(accountTypeName).orElseThrow(() ->
                new BackendException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND, accountTypeName));
        log.debug("Completed getAccountTypeByName operation for account type name: {}. Result: {}", accountTypeName, result);
        return result;
    }
}
