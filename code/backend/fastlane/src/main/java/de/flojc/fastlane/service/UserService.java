package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.*;
import de.flojc.fastlane.repository.UserRepository;
import de.flojc.fastlane.request.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * The UserService class is responsible for managing user data, including retrieval, creation, updating, and deletion of
 * users.
 */
/**
 * The UserService class is responsible for managing user data, including retrieval, creation, updating, and deletion of
 * users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AddressService addressService;
    private final LocationService locationService;
    private final CityService cityService;
    private final CountryService countryService;

    /**
     * The function retrieves all users from the user repository and logs the number of users found.
     *
     * @return The method is returning a List of User objects.
     */
    public List<User> findAll() {
        log.debug("Starting retrieval of all user...");
        List<User> users = userRepository.findAll();
        log.debug("Finished retrieval of all user - {} user found", users.size());
        return users;
    }

    /**
     * The function retrieves a user by their ID from a user repository and throws a backend exception if the user is not
     * found.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of the user that we want to
     * retrieve from the database.
     * @return The method is returning a User object.
     */
    public User findById(Long id) throws BackendException {
        log.debug("Starting retrieval of user with id: {}...", id);
        User user = userRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.USER_NOT_FOUND, id));
        log.debug("Finished retrieval of user with id: {}", id);
        return user;
    }

    /**
     * The function saves a user object to a repository and returns the created user.
     *
     * @param user The "user" parameter is an object of type User that represents the user to be saved in the repository.
     * @return The method is returning the createdUser object, which is of type User.
     */
    public User save(User user) {
        log.debug("Starting to save a user to repository...");
        User createdUser = userRepository.save(user);
        log.debug("Finished saving a user to repository. Id: {}", createdUser.getId());
        return createdUser;
    }

    /**
     * The function updates a user's information based on the provided user request and returns the updated user.
     *
     * @param id The `id` parameter is of type `Long` and represents the unique identifier of the user to be updated.
     * @param userWithUpdatedFields The `userWithUpdatedFields` parameter is an object of type `UserRequest` which contains
     * the updated fields for the user.
     * @return The method is returning an updated User object.
     */
    public User update(Long id, UserRequest userWithUpdatedFields) throws BackendException {
        log.debug("Starting update of user with id: {}...", id);
        userWithUpdatedFields.validateFieldsNotBlank();
        validateAuth(id);


        User userToBeUpdated = userRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.USER_NOT_FOUND, id));

        if (userWithUpdatedFields.getFirstName() != null) {
            userToBeUpdated.setFirstName(userWithUpdatedFields.getFirstName());
        }

        if (userWithUpdatedFields.getLastName() != null) {
            userToBeUpdated.setLastName(userWithUpdatedFields.getLastName());
        }

        if (userWithUpdatedFields.getDateOfBirth() != null) {
            userToBeUpdated.setDateOfBirth(userWithUpdatedFields.getDateOfBirth());
        }

        if (userWithUpdatedFields.getPlaceOfBirth() != null) {
            City city = City.builder()
                    .name(userWithUpdatedFields.getPlaceOfBirth())
                    .build();
            userToBeUpdated.setPlaceOfBirth(cityService.findOrSave(city));
        }
        Address updatedAddress = updateAddress(userToBeUpdated.getAddress(), userWithUpdatedFields);
        userToBeUpdated.setAddress(updatedAddress);

        User updatedUser = userRepository.save(userToBeUpdated);
        log.debug("Finished update of user with id: {}", id);
        return updatedUser;
    }

    /**
     * The function deletes a user with the specified ID and logs the deletion process.
     *
     * @param id The "id" parameter is the unique identifier of the user that needs to be deleted.
     */
    public void delete(Long id) throws BackendException {
        log.debug("Starting deletion of user with id: {}...", id);
        try {
            HelperService.delete(id, userRepository);
            log.debug("Finished deletion of user with id: {}", id);
        } catch (BackendException e) {
            ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
            e.setErrorCode(errorCode);
            e.setDescription(String.format(errorCode.getDescription(), id));
            throw e;
        }
    }

    /**
     * The function finds or saves a user in the user repository.
     *
     * @param user The user object that needs to be found or saved in the database.
     * @return The method is returning a User object.
     */
    public User findOrSave(User user) {
        return HelperService.findOrSave(user, userRepository);
    }

    /**
     * The function updates an address object with the fields provided in a user request and returns the updated address.
     *
     * @param addressToBeUpdated The address object that needs to be updated with the new values provided by the user.
     * @param userWithUpdatedFields An object of type UserRequest that contains the updated fields for the address.
     * @return The method is returning an updated Address object.
     */
    private Address updateAddress(Address addressToBeUpdated, UserRequest userWithUpdatedFields) throws BackendException {
        log.debug("Starting updating address...");
        if (userWithUpdatedFields.getStreet() != null) {
            addressToBeUpdated.setStreet(userWithUpdatedFields.getStreet());
        }

        Location updatedLocation = updateLocation(addressToBeUpdated.getLocation(), userWithUpdatedFields);
        addressToBeUpdated.setLocation(updatedLocation);

        Address updatedAddress = addressService.findOrSave(addressToBeUpdated);
        log.debug("Updated address successfully");
        return updatedAddress;
    }

    /**
     * This function updates the location of a user based on the fields provided in the user request.
     *
     * @param locationToBeUpdated The location object that needs to be updated with the new values.
     * @param userWithUpdatedFields UserRequest object containing the updated fields for the location (countryCode,
     * postalCode, city).
     * @return The method returns an updated Location object.
     */
    private Location updateLocation(Location locationToBeUpdated, UserRequest userWithUpdatedFields) throws BackendException {
        log.debug("Starting updating location...");
        if (userWithUpdatedFields.getCountryCode() != null) {
            String countryCode = userWithUpdatedFields.getCountryCode();
            locationToBeUpdated.setCountry(countryService.findByCountryCode(countryCode));
        }

        if (userWithUpdatedFields.getPostalCode() != null) {
            locationToBeUpdated.setPostalCode(userWithUpdatedFields.getPostalCode());
        }

        if (userWithUpdatedFields.getCity() != null) {
            City city = City.builder()
                    .name(userWithUpdatedFields.getCity())
                    .build();
            locationToBeUpdated.setCity(cityService.findOrSave(city));
        }

        Location updatedLocation = locationService.findOrSave(locationToBeUpdated);
        log.debug("Updated location successfully");
        return updatedLocation;
    }

    /**
     * The function validates the authentication of a user by checking if the account type is "MEMBER" and if the user ID
     * matches the provided ID.
     *
     * @param id The "id" parameter is a Long value representing the user ID that needs to be validated.
     */
    private void validateAuth(Long id) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.getAccountType().getName().equals("MEMBER")) {
            if (!Objects.equals(account.getUser().getId(), id)) {
                throw new AccessDeniedException("");
            }
        }
    }
}