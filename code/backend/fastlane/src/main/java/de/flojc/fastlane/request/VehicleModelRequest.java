package de.flojc.fastlane.request;


import lombok.*;

/**
 * The class VehicleModelRequest is a subclass of Request and represents a request for a vehicle model, including its name,
 * brand, and a link to a picture.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModelRequest extends Request {

    protected String modelName;
    protected String brandName;
    protected String linkToPicture;

}
