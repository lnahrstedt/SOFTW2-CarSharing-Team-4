package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Driver;
import de.flojc.fastlane.request.DriverRequest;
import de.flojc.fastlane.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The DriverController class is a REST controller that handles requests related to drivers, such as checking if a driver
 * exists, retrieving a driver by user ID, and updating a driver's information.
 */
@Slf4j
@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    /**
     * The function checks if a driver with a given license ID exists and returns an appropriate response.
     *
     * @param licenseId The `licenseId` parameter is a string that represents the license ID of a driver.
     * @return The method is returning a ResponseEntity. If the driver with the given licenseId does not exist, it returns
     * a ResponseEntity with HTTP status code 200 (OK) and no body. If there is a BackendException, it returns a
     * ResponseEntity with the corresponding HTTP status code and the BackendException object as the body.
     */
    @GetMapping("/exist/{licenseId}")
    // Tested
    public ResponseEntity<?> doesDriverExist(@PathVariable String licenseId) {
        log.info("Received request to check if driver with licenseId {} exists", licenseId);
        try {
            driverService.existByLicenseId(licenseId);
            log.info("Driver does not exist");
            return ResponseEntity.ok().build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function retrieves a driver based on the user ID and account ID, with authorization checks for admin or
     * employee roles, or if the account ID matches the authenticated user's ID.
     *
     * @param userId The userId parameter is a Long value that represents the unique identifier of a user. It is used to
     * retrieve a driver associated with the specified user.
     * @param accountId The `accountId` parameter is used to specify the ID of the account for which the driver information
     * is being retrieved.
     * @return The method is returning a ResponseEntity object. If the driver is successfully fetched from the user with
     * the given userId, the ResponseEntity will contain the driver object and have a status code of 200 (OK). If there is
     * a BackendException, the ResponseEntity will contain the exception object and have a status code based on the error
     * code of the exception.
     */
    @GetMapping("/user/{userId}/{accountId}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #accountId == authentication.principal.id")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId, @PathVariable Long accountId) {
        log.info("Received request to get driver from user with id: {}", userId);
        try {
            Driver driver = driverService.findByUserId(userId, true);
            log.info("Fetched driver from user with id: {}", userId);
            return ResponseEntity.ok(driver);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function updates a driver's information based on the provided ID and account ID.
     *
     * @param id The "id" parameter in the @PatchMapping annotation represents the ID of the driver that needs to be
     * updated.
     * @param accountId The `accountId` parameter is the ID of the account associated with the driver. It is used in the
     * `PreAuthorize` annotation to check if the authenticated user has the authority to update the driver. If the user has
     * the authority of "ADMIN" or "EMPLOYEE", they are allowed to update
     * @param driverRequest The `driverRequest` parameter is of type `DriverRequest` and represents the request body
     * containing the updated driver information.
     * @return The method is returning a ResponseEntity object. If the update operation is successful, it returns a
     * ResponseEntity with the updated Driver object as the body and a status code of 200 (OK). If there is an exception
     * (BackendException), it returns a ResponseEntity with the exception object as the body and the corresponding HTTP
     * status code based on the error code of the exception.
     */
    @PatchMapping("{id}/{accountId}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #accountId == authentication.principal.id")
    public ResponseEntity<?> update(@PathVariable Long id, @PathVariable Long accountId, @RequestBody DriverRequest driverRequest) {
        log.info("Received request to update driver with id: {}", id);
        try {
            Driver updatedDriver = driverService.update(id, driverRequest, true);
            log.info("Updated driver with id: {}", id);
            return ResponseEntity.ok(updatedDriver);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}
