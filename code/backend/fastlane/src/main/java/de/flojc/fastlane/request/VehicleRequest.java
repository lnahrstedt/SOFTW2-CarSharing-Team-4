package de.flojc.fastlane.request;

import lombok.*;

/**
 * The VehicleRequest class is a subclass of the Request class and represents a request for vehicle information.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest extends Request {

    protected String vehicleModel;
    protected String numberPlate;
    protected Double mileage;
    protected Double latitude;
    protected Double longitude;
    protected Short constructionYear;
    protected Character vehicleCategory;
    protected Character vehicleType;
    protected Character vehicleTransmission;
    protected Character vehicleFuel;
    protected Boolean hasAc;
    protected Boolean hasNavigation;
    protected Boolean hasCruiseControl;
    protected Boolean hasDrivingAssistant;

}
