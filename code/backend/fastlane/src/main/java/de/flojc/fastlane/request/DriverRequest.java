package de.flojc.fastlane.request;

import lombok.*;

/**
 * The class "DriverRequest" extends the class "Request" and includes additional properties such as licenseId, userId, and
 * fareTypeName.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest extends Request {

    protected String licenseId;
    protected Long userId;
    protected String fareTypeName;

}