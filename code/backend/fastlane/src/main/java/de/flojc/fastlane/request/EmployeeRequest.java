package de.flojc.fastlane.request;

import lombok.*;

/**
 * The class EmployeeRequest extends the Request class and includes properties for employeeId and userId.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest extends Request {

    protected String employeeId;
    protected Long userId;

}