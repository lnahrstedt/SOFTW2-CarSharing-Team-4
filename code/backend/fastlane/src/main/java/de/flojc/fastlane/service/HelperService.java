package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * The HelperService class provides methods for finding or saving objects in a repository and deleting objects from a
 * repository.
 */
@Slf4j
public final class HelperService {

    /**
     * The function `findOrSave` takes an object and a JpaRepository, and either finds the object in the repository or
     * saves a new object if it doesn't exist.
     *
     * @param object The object that you want to find or save in the repository. It can be any type.
     * @param jpaRepository The `jpaRepository` parameter is an instance of a class that implements the `JpaRepository`
     * interface. This interface is typically used in Spring Data JPA to provide CRUD operations for a specific entity
     * type. It allows you to perform common database operations such as saving, updating, deleting, and querying entities
     * @return The method is returning an object of type T, which is either the object found in the repository or the newly
     * saved object.
     */
    public static <T, R extends JpaRepository<T, ?>> T findOrSave(T object, R jpaRepository) {
        log.debug("Starting process to find object in repository or save new object: {}...", object.toString());
        String ignorePath = "id";
        Example<T> example = Example.of(object, ExampleMatcher.matching().withIgnorePaths(ignorePath));
        Optional<T> optionalResult = jpaRepository.findAll(example).stream().findFirst();

        if (optionalResult.isPresent()) {
            log.debug("Object found in the repository: {}", optionalResult.get());
        } else {
            log.debug("Object not found in the repository.");
        }

        T result = optionalResult.orElseGet(() -> {
            T savedObject = jpaRepository.save(object);
            log.debug("New object saved in the repository: {}", savedObject);
            return savedObject;
        });

        log.debug("Finished process to find or safe object. Result: {}", result);
        return result;
    }

    /**
     * The function deletes an entity with a given ID from a repository.
     *
     * @param id The `id` parameter is the identifier of the entity that needs to be deleted from the repository. It is of
     * type `ID`, which represents the type of the identifier.
     * @param repository The "repository" parameter is an instance of a class that implements the JpaRepository interface.
     * It is used to perform CRUD operations on a specific entity type (T) using the provided ID type (ID).
     */
    public static <T, ID, R extends JpaRepository<T, ID>> void delete(ID id, R repository) throws BackendException {
        log.debug("Starting markReservationAsCanceled operation for id: {} in repository: {}...", id, repository.getClass().getName());
        T entity = repository.findById(id).orElseThrow(BackendException::new);
        repository.delete(entity);
        log.debug("Completed markReservationAsCanceled operation for id: {} in repository: {}", id, repository.getClass().getName());
    }
}