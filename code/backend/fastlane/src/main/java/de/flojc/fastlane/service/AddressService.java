package de.flojc.fastlane.service;

import de.flojc.fastlane.model.Address;
import de.flojc.fastlane.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * The AddressService class is a service that finds or saves an Address object using the AddressRepository and logs the
 * operation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    /**
     * The function finds or saves an address and returns the result.
     *
     * @param address The "address" parameter is an object of type Address, which represents a physical address.
     * @return The method is returning an instance of the Address class.
     */
    public Address findOrSave(Address address) {
        log.debug("Starting findOrSave operation for address: {}...", address);
        Address result = HelperService.findOrSave(address, addressRepository);
        log.debug("Completed findOrSave operation for address: {}. Result: {}", address, result);
        return result;
    }
}