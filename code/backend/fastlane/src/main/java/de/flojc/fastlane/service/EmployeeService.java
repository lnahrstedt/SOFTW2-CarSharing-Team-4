package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Employee;
import de.flojc.fastlane.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The EmployeeService class is a Java service class that provides methods for retrieving, saving, and deleting employee
 * data from a repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * The function retrieves all employees from the employee repository and logs the number of employees found.
     *
     * @return The method is returning a List of Employee objects.
     */
    public List<Employee> findAll() {
        log.debug("Starting retrieval of all employees...");
        List<Employee> employees = employeeRepository.findAll();
        log.debug("Finished retrieval of all employees - {} employees found", employees.size());
        return employees;
    }

    /**
     * The function retrieves an employee from the employee repository based on their ID and throws a BackendException if
     * the employee is not found.
     *
     * @param id The "id" parameter is a String that represents the unique identifier of an employee.
     * @return The method is returning an instance of the Employee class.
     */
    public Employee findById(String id) throws BackendException {
        log.debug("Starting retrieval of employee with id: {}...", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.EMPLOYEE_NOT_FOUND, id));
        log.debug("Finished retrieval of employee with id: {}", id);
        return employee;
    }

    /**
     * The function retrieves an employee from the employee repository based on the given user id.
     *
     * @param userId The userId parameter is of type Long and represents the unique identifier of an employee.
     * @return The method is returning an instance of the Employee class.
     */
    public Employee findByUserId(Long userId) throws BackendException {
        log.debug("Starting retrieval of employee with user id: {}...", userId);
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(() -> new BackendException(ErrorCode.EMPLOYEE_NOT_FOUND, userId));
        log.debug("Finished retrieval of employee with user id: {}", userId);
        return employee;
    }

    /**
     * The function saves a new employee to the repository and returns the created employee.
     *
     * @param employee The employee object that needs to be saved to the repository.
     * @return The method is returning the createdEmployee object.
     */
    public Employee save(Employee employee) {
        log.debug("Starting to save a new employee to the repository...");
        Employee createdEmployee = employeeRepository.save(employee);
        log.debug("Finished saving a new employee with id: {}", createdEmployee.getId());
        return createdEmployee;
    }


    /**
     * The function deletes an employee with the given ID and throws a BackendException if the employee is not found.
     *
     * @param id The "id" parameter is the unique identifier of the employee that needs to be deleted.
     */
    public void delete(String id) throws BackendException {
        log.debug("Starting deletion of employee with id: {}...", id);
        try {
            HelperService.delete(id, employeeRepository);
            log.debug("Finished deletion of employee with id: {}", id);
        } catch (BackendException e) {
            ErrorCode errorCode = ErrorCode.EMPLOYEE_NOT_FOUND;
            e.setErrorCode(errorCode);
            e.setDescription(String.format(errorCode.getDescription(), id));
            throw e;
        }
    }

    /**
     * The function checks if an employee with a given ID exists and throws an exception if they already exist.
     *
     * @param id The "id" parameter is a unique identifier for an employee. It is used to check if an employee with the
     * given id already exists in the employee repository.
     */
    public void existById(String id) throws BackendException {
        log.debug("Checking existence of employee with id: {}...", id);
        boolean exists = employeeRepository.existsById(id);
        log.debug("Existence of employee with id: {} checked. Exists: {}", id, exists);
        if (exists) {
            throw new BackendException(ErrorCode.EMPLOYEE_ALREADY_EXIST, id);
        }
    }
}