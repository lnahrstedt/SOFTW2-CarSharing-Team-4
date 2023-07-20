package de.flojc.fastlane.service;

import de.flojc.fastlane.model.Equipment;
import de.flojc.fastlane.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The EquipmentService class is a service that finds or saves equipment objects using an EquipmentRepository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    /**
     * The function finds or saves an equipment object and returns the result.
     *
     * @param equipment The "equipment" parameter is an object of the Equipment class.
     * @return The method is returning an object of type Equipment.
     */
    public Equipment findOrSave(Equipment equipment) {
        log.debug("Starting findOrSave operation for equipment: {}...", equipment);
        Equipment result = HelperService.findOrSave(equipment, equipmentRepository);
        log.debug("Completed findOrSave operation for equipment: {}. Result: {}", equipment, result);
        return result;
    }
}