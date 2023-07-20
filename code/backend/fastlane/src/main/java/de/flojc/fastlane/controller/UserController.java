package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.User;
import de.flojc.fastlane.request.UserRequest;
import de.flojc.fastlane.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The UserController class is a REST controller that handles requests related to user management, including retrieving all
 * users, retrieving a user by ID, and updating a user's information.
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * This function retrieves all users and returns them as a response entity.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> getAll() {
        log.info("Received request to get all users");
        List<User> users = userService.findAll();
        log.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * The function retrieves a user by their ID and returns a ResponseEntity with the user object if found, or an error
     * message if not found.
     *
     * @param id The "id" parameter is a Long value that represents the unique identifier of a user. It is used to retrieve
     * the user with the specified id from the database.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{id}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') || #id == authentication.principal.id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received request to get user with id: {}", id);
        try {
            User user = userService.findById(id);
            log.info("Fetched user with id: {}", id);
            return ResponseEntity.ok(user);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The function updates a user with the specified ID using the provided updated fields and returns the updated user or
     * an error response.
     *
     * @param id The "id" parameter is a Long value that represents the unique identifier of the user to be updated. It is
     * extracted from the path variable in the URL.
     * @param userWithUpdatedFields The parameter `userWithUpdatedFields` is of type `UserRequest` and represents the
     * updated fields of a user. It is annotated with `@RequestBody`, which means that the data for this parameter will be
     * extracted from the request body of the HTTP request.
     * @return The method is returning a ResponseEntity object.
     */
    @PatchMapping("/{id}")
    //Tested
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest userWithUpdatedFields) {
        log.info("Received request to update user with id: {}", id);
        try {
            User updatedUser = userService.update(id, userWithUpdatedFields);
            return ResponseEntity.ok(updatedUser);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}
