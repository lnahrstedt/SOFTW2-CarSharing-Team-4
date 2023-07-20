package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.*;
import de.flojc.fastlane.request.RegisterRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The `RegistrationService` class is responsible for handling the registration process for accounts, converting register
 * requests to accounts, and validating the registration requests.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AccountService accountService;
    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocationService locationService;
    private final AddressService addressService;
    private final AccountTypeService accountTypeService;
    private final FareTypeService fareTypeService;
    private final DriverService driverService;
    private final EmployeeService employeeService;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    /**
     * The function registers a new account, converts the registration request to an account object, saves it, generates an
     * authentication response, and returns it.
     *
     * @param registerRequest The `registerRequest` parameter is an object of type `RegisterRequest` which contains the
     * information required to register a new account. This could include fields such as email, password, username, etc.
     * @param isMemberRegistration The parameter "isMemberRegistration" is a boolean value that indicates whether the
     * registration is for a regular user or a member.
     * @return The method is returning an AuthenticationResponse object.
     */
    public AuthenticationResponse register(RegisterRequest registerRequest, boolean isMemberRegistration) throws BackendException {
        log.debug("Starting registration process for account with email: {}...", registerRequest.getEmail());
        Account savedAccount = convertRegisterRequestToAccountAndSave(registerRequest, isMemberRegistration);
        AuthenticationResponse authenticationResponse = tokenService.generateAuthenticationResponse(savedAccount);
        log.debug("Finished registration process for account with email: {}", registerRequest.getEmail());
        return authenticationResponse;
    }

    /**
     * The function converts a list of RegisterRequest objects to a list of Account objects and saves them, with an option
     * to specify if it is for member registration.
     *
     * @param registerRequests A list of RegisterRequest objects, which contain information about user registration
     * details.
     * @param isMemberRegistration A boolean value indicating whether the registration is for a member or not.
     */
    public void convertListOfRegisterRequestToListOfAccountAndSave(List<RegisterRequest> registerRequests, boolean isMemberRegistration) throws BackendException {
        log.debug("Converting list of RegisterRequest to list of Account...");
        for (RegisterRequest registerRequest : registerRequests) {
            convertRegisterRequestToAccountAndSave(registerRequest, isMemberRegistration);
        }
        log.debug("Finished converting list of RegisterRequest to list of Account");
    }

    /**
     * The function converts a RegisterRequest object into an Account object, validates the request, builds the necessary
     * user, account type, and account objects, and saves the account in the database.
     *
     * @param newAccount The newAccount parameter is of type RegisterRequest and contains the information for creating a
     * new account. It includes fields such as email, phone, password, and typeName.
     * @param isMemberRegistration A boolean value indicating whether the registration is for a member or not.
     * @return The method is returning an instance of the Account class.
     */
    public Account convertRegisterRequestToAccountAndSave(RegisterRequest newAccount, boolean isMemberRegistration) throws BackendException {
        log.debug("Starting convertRegisterRequestToAccountAndSave operation for RegisterRequest: {}, isMemberRegistration: {}...", newAccount, isMemberRegistration);
        validateRequest(newAccount, isMemberRegistration);
        User user = buildUser(newAccount);
        validateAccountTypeNotUsedTwice(user.getId(), newAccount, isMemberRegistration);
        AccountType accountType = buildAccountType(isMemberRegistration, newAccount.getTypeName());
        Account account = buildAccount(accountType, newAccount.getEmail(), newAccount.getPhone(), newAccount.getPassword(), user);

        if (isMemberRegistration) {
            FareType fareType = buildFareType(newAccount.getTypeName());
            Driver driver = buildDriver(fareType, newAccount.getId(), user);
            driverService.save(driver);
            log.debug("Driver from request saved: {}", driver);
        } else {
            Employee employee = buildEmployee(newAccount.getId(), user);
            log.debug("Employee from request saved: {}", employee);
            employeeService.save(employee);
        }

        Account savedAccount = accountService.save(account);
        log.debug("Done converting RegisterRequest to an account. Result: {}", savedAccount);
        return savedAccount;
    }

    /**
     * The function builds an Employee object with the given id and user.
     *
     * @param id The id parameter is a unique identifier for the employee. It could be a string or any other data type that
     * uniquely identifies the employee.
     * @param user The "user" parameter is an instance of the User class. It represents the user associated with the
     * employee.
     * @return The method is returning an instance of the Employee class.
     */
    private Employee buildEmployee(String id, User user) {
        return Employee.builder()
                .id(id)
                .user(user)
                .build();
    }

    /**
     * The function builds a Driver object with the given fare type, license ID, and user.
     *
     * @param fareType The fareType parameter is of type FareType, which represents the type of fare that the driver
     * offers. It could be a fixed fare, a metered fare, or any other type of fare that the system supports.
     * @param licenseId The licenseId parameter is a String that represents the license ID of the driver.
     * @param user The "user" parameter is an instance of the User class, which represents the user associated with the
     * driver.
     * @return The method is returning a Driver object.
     */
    private Driver buildDriver(FareType fareType, String licenseId, User user) {

        return Driver.builder()
                .fareType(fareType)
                .licenseId(licenseId)
                .user(user)
                .build();
    }

    /**
     * The function builds a FareType object by retrieving it from the fareTypeService based on the given fareTypeName.
     *
     * @param fareTypeName The fareTypeName parameter is a String that represents the name of a fare type.
     * @return The method is returning a FareType object.
     */
    private FareType buildFareType(String fareTypeName) throws BackendException {
        return fareTypeService.getFareTypeByName(fareTypeName);
    }

    /**
     * The function builds and returns an Account object with the given parameters.
     *
     * @param accountType The account type of the account being created. It could be a personal account, business account,
     * or any other type of account defined in the AccountType enum.
     * @param email The email parameter is a String that represents the email address associated with the account.
     * @param phone The "phone" parameter is a String that represents the phone number associated with the account.
     * @param password The "password" parameter is a String that represents the password for the account.
     * @param user The "user" parameter is an instance of the User class. It represents the user associated with the
     * account being created.
     * @return The method is returning an instance of the Account class.
     */
    private Account buildAccount(AccountType accountType, String email, String phone, String password, User user) {

        return Account.builder()
                .creationDate(LocalDate.now())
                .accountType(accountType)
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode(password))
                .user(user)
                .build();
    }

    /**
     * The function builds an AccountType object based on the given parameters and retrieves it from the
     * accountTypeService.
     *
     * @param isMember A boolean value indicating whether the account is a member account or not.
     * @param type The "type" parameter is a string that represents the name of the account type.
     * @return The method is returning an instance of the AccountType class.
     */
    private AccountType buildAccountType(boolean isMember, String type) throws BackendException {
        String accountTypeName = isMember ? "MEMBER" : type;
        return accountTypeService.getAccountTypeByName(accountTypeName);
    }

    /**
     * The function builds a User object using the information from a RegisterRequest object and saves it in the
     * userService.
     *
     * @param registerRequest The `registerRequest` parameter is an object of type `RegisterRequest`. It contains
     * information required to register a user, such as their first name, last name, date of birth, place of birth, and
     * address.
     * @return The method is returning a User object.
     */
    private User buildUser(RegisterRequest registerRequest) throws BackendException {

        Address address = buildAddress(registerRequest);
        City placeOfBirth = buildCity(registerRequest.getPlaceOfBirth());

        User user = User.builder()
                .address(address)
                .dateOfBirth(LocalDate.parse(registerRequest.getDateOfBirth()))
                .placeOfBirth(placeOfBirth)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();
        return userService.findOrSave(user);
    }

    /**
     * The function builds an Address object using the RegisterRequest object and saves it using the addressService.
     *
     * @param registerRequest An object of type RegisterRequest, which contains information about a user's registration
     * details such as street address.
     * @return The method is returning an instance of the Address class.
     */
    private Address buildAddress(RegisterRequest registerRequest) throws BackendException {

        Location location = buildLocation(registerRequest);

        Address address = Address.builder()
                .location(location)
                .street(registerRequest.getStreet())
                .build();
        return addressService.findOrSave(address);
    }

    /**
     * The function builds a location object using the provided register request data and saves it in the location service.
     *
     * @param registerRequest An object of type RegisterRequest, which contains information about a user's registration
     * details such as city, postal code, and country code.
     * @return The method is returning a Location object.
     */
    private Location buildLocation(RegisterRequest registerRequest) throws BackendException {

        City city = buildCity(registerRequest.getCity());
        Country country = buildCountry(registerRequest.getCountryCode());

        Location location = Location.builder()
                .city(city)
                .postalCode(registerRequest.getPostalCode())
                .country(country)
                .build();
        return locationService.findOrSave(location);
    }

    /**
     * The function builds a Country object by finding it using the given country code.
     *
     * @param countryCode The countryCode parameter is a string that represents the code of a country.
     * @return The method is returning an object of type Country.
     */
    private Country buildCountry(String countryCode) throws BackendException {
        return countryService.findByCountryCode(countryCode);
    }


    /**
     * The function builds a City object with the given cityName and returns the City object after finding or saving it
     * using the cityService.
     *
     * @param cityName The cityName parameter is a String that represents the name of the city that we want to build.
     * @return The method is returning a City object.
     */
    private City buildCity(String cityName) {
        City city = City.builder()
                .name(cityName)
                .build();
        return cityService.findOrSave(city);
    }

    /**
     * The function validates a register request by checking if the required fields are not null or blank, and then
     * checking if the provided ID and email already exist in the system.
     *
     * @param registerRequest An object of type RegisterRequest, which contains the information needed for registration
     * (such as name, email, etc.).
     * @param isMember A boolean value indicating whether the registerRequest belongs to a member or not.
     */
    private void validateRequest(RegisterRequest registerRequest, boolean isMember) throws BackendException {
        registerRequest.validateFieldsNotNullOrBlank();
        if (isMember) {
            driverService.existByLicenseId(registerRequest.getId());
        } else {
            employeeService.existById(registerRequest.getId());
        }
        accountService.existByEmail(registerRequest.getEmail());
    }

    /**
     * The function validates that a user's requested account type is not used twice and does not conflict with existing
     * account types.
     *
     * @param userId The userId parameter is the unique identifier of the user for whom the account type is being
     * validated.
     * @param registerRequest The `registerRequest` parameter is an object of type `RegisterRequest` which contains
     * information about the user's registration request, such as the requested account type.
     * @param isMemberRegistration A boolean flag indicating whether the registration is for a member or not.
     */
    private void validateAccountTypeNotUsedTwice(Long userId, RegisterRequest registerRequest, boolean isMemberRegistration) throws BackendException {
        String requestedAccountType = registerRequest.getTypeName().toLowerCase();

        if (!isMemberRegistration && !requestedAccountType.equals("admin") && !requestedAccountType.equals("employee")) {
            throw new BackendException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND, requestedAccountType);
        }

        List<Account> accountsOfUser = accountService.findAccountsByUserId(userId);

        Set<String> usedAccountTypes = accountsOfUser.stream()
                .map(account -> account.getAccountType().getName().toLowerCase())
                .collect(Collectors.toSet());

        if (usedAccountTypes.isEmpty()) {
            return;
        }

        if (isMemberRegistration && usedAccountTypes.contains("member")) {
            throw new BackendException(ErrorCode.ACCOUNT_TYPE_USED_TWICE, "MEMBER", userId);
        }

        if (!isMemberRegistration) {
            if (usedAccountTypes.contains(requestedAccountType)) {
                throw new BackendException(ErrorCode.ACCOUNT_TYPE_USED_TWICE, requestedAccountType, userId);
            }

            if (usedAccountTypes.contains("admin") && requestedAccountType.equals("employee") || usedAccountTypes.contains("employee") && requestedAccountType.equals("admin")) {
                throw new BackendException(ErrorCode.ACCOUNT_TYPE_CONFLICT, userId);
            }
        }
    }
}
