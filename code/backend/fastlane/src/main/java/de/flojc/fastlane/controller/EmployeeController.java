package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Employee;
import de.flojc.fastlane.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The EmployeeController class is a REST controller that handles requests related to employees, such as retrieving all
 * employees or finding an employee by their ID.
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;

    /**
     * This function retrieves all employees and returns them as a response entity.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> findAll() {
        log.info("Received request to get all employees");
        List<Employee> employees = employeeService.findAll();
        log.info("Fetched {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    /**
     * This function retrieves an employee by their ID and returns a ResponseEntity with the employee object if found, or
     * an error response if not found.
     *
     * @param id The "id" parameter is a path variable that represents the unique identifier of an employee. It is used to
     * retrieve the employee with the specified id from the database.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{id}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable String id) {
        log.info("Received request to get employee with id: {}", id);
        try {
            Employee employee = employeeService.findById(id);
            log.info("Fetched employee with id: {}", id);
            return ResponseEntity.ok(employee);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}