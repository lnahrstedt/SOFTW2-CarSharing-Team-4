package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.helper.AuthHelper;
import de.flojc.fastlane.model.Driver;
import de.flojc.fastlane.model.FareType;
import de.flojc.fastlane.model.User;
import de.flojc.fastlane.repository.DriverRepository;
import de.flojc.fastlane.request.DriverRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The DriverService class is responsible for managing and performing operations on Driver objects, such as retrieving,
 * saving, updating, and deleting drivers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final UserService userService;
    private final FareTypeService fareTypeService;

    /**
     * The function retrieves all drivers from the driver repository and logs the number of drivers found.
     *
     * @return The method is returning a List of Driver objects.
     */
    public List<Driver> findAll() {
        log.debug("Starting retrieval of all drivers...");
        List<Driver> drivers = driverRepository.findAll();
        log.debug("Finished retrieval of all drivers - {} drivers found", drivers.size());
        return drivers;
    }

    /**
     * The function retrieves a driver by their user ID, optionally validating their authentication.
     *
     * @param userId The userId parameter is the unique identifier of the user for whom we want to find the driver.
     * @param validateAuth A boolean flag indicating whether to validate the authentication or not. If set to true, the
     * authentication will be validated using the AuthHelper class. If set to false, the driver will be retrieved directly
     * from the driverRepository without authentication validation.
     * @return The method is returning a Driver object.
     */
    public Driver findByUserId(Long userId, boolean validateAuth) throws BackendException {
        log.debug("Starting retrieval of driver with user id: {}...", userId);
        Driver driver;
        if (validateAuth) {
            driver = AuthHelper.validateAuthAndGetDriverByUserId(userId, driverRepository);
        } else {
            driver = driverRepository.findByUserId(userId).orElseThrow(() -> new BackendException(ErrorCode.DRIVER_NOT_FOUND, userId));
        }
        log.debug("Finished retrieval of driver with user id: {}", userId);
        return driver;
    }

    /**
     * The function findById retrieves a Driver object from the driverRepository based on the given id and throws a
     * BackendException if the driver is not found.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of a driver.
     * @return The method is returning a Driver object.
     */
    public Driver findById(Long id) throws BackendException {
        log.debug("Starting findById operation for id: {}...", id);
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.DRIVER_NOT_FOUND, id));
        log.debug("Completed findById operation for id: {}", id);
        return driver;
    }

    /**
     * The function saves a driver object to the repository and returns the saved driver.
     *
     * @param driver The "driver" parameter is an object of the Driver class, which represents a driver with a license.
     * @return The method is returning the saved driver object.
     */
    public Driver save(Driver driver) {
        log.debug("Starting to save a driver with license id: {} to the repository...", driver.getLicenseId());
        Driver savedDriver = driverRepository.save(driver);
        log.debug("Finished saving a driver to the repository");
        return savedDriver;
    }

    /**
     * This function updates a driver's information based on the provided driver request and optionally validates the
     * authentication.
     *
     * @param id The id parameter is the unique identifier of the driver that needs to be updated.
     * @param driverWithUpdatedFields The `driverWithUpdatedFields` parameter is an object of type `DriverRequest` that
     * contains the updated fields for the driver.
     * @param validateAuth The validateAuth parameter is a boolean value that determines whether authentication should be
     * validated before updating the driver. If validateAuth is true, the method will call the
     * AuthHelper.validateAuthAndGetDriverById() method to validate the authentication and retrieve the driver with the
     * specified id. If validateAuth is false, the
     * @return The method is returning a Driver object.
     */
    public Driver update(Long id, DriverRequest driverWithUpdatedFields, boolean validateAuth) throws BackendException {
        log.debug("Starting update of driver with id: {}...", id);
        driverWithUpdatedFields.validateFieldsNotBlank();

        Driver driverToBeUpdated;

        if (validateAuth) {
            driverToBeUpdated = AuthHelper.validateAuthAndGetDriverById(id, driverRepository);
        } else {
            driverToBeUpdated = findById(id);
        }


        if (driverWithUpdatedFields.getLicenseId() != null) {
            driverToBeUpdated.setLicenseId(driverWithUpdatedFields.getLicenseId());
        }

        if (driverWithUpdatedFields.getFareTypeName() != null) {
            String fareTypeName = driverWithUpdatedFields.getFareTypeName();
            FareType fareType = fareTypeService.getFareTypeByName(fareTypeName);
            driverToBeUpdated.setFareType(fareType);
        }

        log.debug("Finished update of driver with id: {}", id);
        return driverRepository.save(driverToBeUpdated);
    }

    /**
     * The function deletes a driver with a specific ID and logs the deletion process.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of the driver that needs to be
     * deleted.
     */
    public void delete(Long id) throws BackendException {
        log.debug("Starting deletion of driver with id: {}...", id);
        try {
            HelperService.delete(id, driverRepository);
            log.debug("Finished deletion of driver with id: {}", id);
        } catch (BackendException e) {
            ErrorCode errorCode = ErrorCode.DRIVER_NOT_FOUND;
            e.setErrorCode(errorCode);
            e.setDescription(String.format(errorCode.getDescription(), id));
            throw e;
        }
    }

    /**
     * The function checks if a driver with a given license ID exists and throws an exception if it does.
     *
     * @param licenseId The parameter "licenseId" is a string that represents the license ID of a driver.
     */
    public void existByLicenseId(String licenseId) throws BackendException {
        log.debug("Checking existence of driver with license id: {}...", licenseId);
        boolean exists = driverRepository.existsByLicenseId(licenseId);
        log.debug("Existence of driver with license id: {} checked. Exists: {}", licenseId, exists);
        if (exists) {
            throw new BackendException(ErrorCode.DRIVER_LICENSE_ALREADY_IN_USE, licenseId);
        }
    }

    /**
     * The function converts a list of DriverRequest objects to a list of Driver objects and saves them.
     *
     * @param driverRequests A list of DriverRequest objects.
     */
    public void convertListOfDriverRequestToListOfDriver(List<DriverRequest> driverRequests) throws BackendException {
        log.debug("Converting list of DriverRequest to list of Driver...");
        for (DriverRequest driverRequest : driverRequests) {
            convertDriverRequestToDriverAndSave(driverRequest);
        }
        log.debug("Finished converting list of DriverRequest to list of Driver");
    }

    /**
     * This function converts a DriverRequest object into a Driver object, saves it, and returns the saved Driver object.
     *
     * @param driverRequest An object of type DriverRequest, which contains information about a driver such as user ID,
     * fare type name, and license ID.
     * @return The method is returning a Driver object.
     */
    private Driver convertDriverRequestToDriverAndSave(DriverRequest driverRequest) throws BackendException {
        log.debug("Converting DriverRequest to Driver...");
        Long userId = driverRequest.getUserId();
        String fareTypeName = driverRequest.getFareTypeName();

        User user = userService.findById(userId);
        FareType fareType = fareTypeService.getFareTypeByName(fareTypeName);
        Driver driver = Driver.builder().licenseId(driverRequest.getLicenseId()).user(user).fareType(fareType).build();
        log.debug("Finished converting DriverRequest to Driver");
        return save(driver);
    }
}