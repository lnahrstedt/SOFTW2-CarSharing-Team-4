package de.flojc.fastlane;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.loading.LoadingIndicator;
import de.flojc.fastlane.model.*;
import de.flojc.fastlane.repository.*;
import de.flojc.fastlane.request.RegisterRequest;
import de.flojc.fastlane.request.ReservationRequest;
import de.flojc.fastlane.request.VehicleModelRequest;
import de.flojc.fastlane.request.VehicleRequest;
import de.flojc.fastlane.resources.AsciiArt;
import de.flojc.fastlane.resources.JsonFileNames;
import de.flojc.fastlane.service.RegistrationService;
import de.flojc.fastlane.service.ReservationService;
import de.flojc.fastlane.service.VehicleModelService;
import de.flojc.fastlane.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * The `FastlaneApplication` class is the main class of a Java Spring Boot application that loads data from JSON files into
 * various repositories based on configuration properties.
 */
@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,})
public class FastlaneApplication {

    private static LocalDateTime startingTime;
    LoadingIndicator loadingIndicator = new LoadingIndicator();
    @Value("${load-demo-data}")
    private boolean loadDemoData;
    @Value("${load-mock-data}")
    private boolean loadMockData;
    @Value("${load-required-data}")
    private boolean loadRequiredData;

    /**
     * The main function starts the Fastlane backend, logs the time it took to start, and prints a ready message.
     */
    public static void main(String[] args) {
        startingTime = LocalDateTime.now();
        SpringApplication.run(FastlaneApplication.class, args);
        log.info("Started fastlane backend in " + Duration.between(startingTime, LocalDateTime.now()).toMillis() + " milliseconds!");
        System.out.println(AsciiArt.READY);
    }

    /**
     * The function reads countries from a JSON file and saves them to a repository, but only if a certain condition is
     * met.
     *
     * @param countryRepository The `countryRepository` is an instance of a repository class that is responsible for
     * interacting with the database or data storage to perform CRUD (Create, Read, Update, Delete) operations on the
     * `Country` entity. It provides methods like `saveAll()` to save a list of countries to the database
     * @return The method is returning a CommandLineRunner.
     */
    @Bean
    public CommandLineRunner readCountriesToRepo(CountryRepository countryRepository) {
        if (loadRequiredData && countryRepository.findAll().isEmpty()) {
            startingTime = LocalDateTime.now();
            System.out.println(AsciiArt.INIT_FASTLANE);
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.COUNTRIES, new TypeReference<List<Country>>() {
            }, countryRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads account types from a JSON file and saves them to a repository, but only if the loadRequiredData
     * flag is set to true.
     *
     * @param accountTypeRepository An instance of the AccountTypeRepository class, which is responsible for interacting
     * with the database to perform CRUD operations on AccountType entities.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readAccountTypesToRepo(AccountTypeRepository accountTypeRepository) {
        if (loadRequiredData && accountTypeRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.ACCOUNT_TYPES, new TypeReference<List<AccountType>>() {
            }, accountTypeRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads fare types from a JSON file and saves them to a repository, but only if the loadRequiredData flag
     * is set to true.
     *
     * @param fareTypeRepository The `fareTypeRepository` is an instance of the `FareTypeRepository` interface, which is
     * responsible for interacting with the database to perform CRUD operations on the `FareType` entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readFareTypesToRepo(FareTypeRepository fareTypeRepository) {
        if (loadRequiredData && fareTypeRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.FARE_TYPES, new TypeReference<List<FareType>>() {
            }, fareTypeRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle category data from a JSON file and saves it to a repository, but only if the
     * loadRequiredData flag is set to true.
     *
     * @param vehicleCategoryRepository This parameter is an instance of the VehicleCategoryRepository interface. It is
     * used to interact with the database and perform CRUD operations on the VehicleCategory entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleCategoryToRepo(VehicleCategoryRepository vehicleCategoryRepository) {
        if (loadRequiredData && vehicleCategoryRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_CATEGORY, new TypeReference<List<VehicleCategory>>() {
            }, vehicleCategoryRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle types from a JSON file and saves them to a repository, but only if the loadRequiredData
     * flag is set to true.
     *
     * @param vehicleTypeRepository The vehicleTypeRepository is an instance of the VehicleTypeRepository interface, which
     * is responsible for interacting with the database to perform CRUD operations on the VehicleType entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleTypesToRepo(VehicleTypeRepository vehicleTypeRepository) {
        if (loadRequiredData && vehicleTypeRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_TYPE, new TypeReference<List<VehicleType>>() {
            }, vehicleTypeRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle transmission data from a JSON file and saves it to a repository, but only if
     * loadRequiredData is true.
     *
     * @param vehicleTransmissionRepository This parameter is an instance of the VehicleTransmissionRepository interface.
     * It is used to interact with the database and perform CRUD operations on the VehicleTransmission entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleTransmissionToRepo(VehicleTransmissionRepository vehicleTransmissionRepository) {
        if (loadRequiredData && vehicleTransmissionRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_TRANSMISSION, new TypeReference<List<VehicleTransmission>>() {
            }, vehicleTransmissionRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle fuel data from a JSON file and saves it to a repository, but only if loadRequiredData is
     * true.
     *
     * @param vehicleFuelRepository The vehicleFuelRepository is an instance of the VehicleFuelRepository interface, which
     * is responsible for interacting with the database to perform CRUD operations on the VehicleFuel entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleFuelToRepo(VehicleFuelRepository vehicleFuelRepository) {
        if (loadRequiredData && vehicleFuelRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_FUEL, new TypeReference<List<VehicleFuel>>() {
            }, vehicleFuelRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads reservation states from a JSON file and saves them to a repository, but only if the
     * loadRequiredData flag is set to true.
     *
     * @param reservationStateRepository The `reservationStateRepository` is an instance of the
     * `ReservationStateRepository` interface, which is responsible for interacting with the database to perform CRUD
     * operations on `ReservationState` entities.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readReservationStatesToRepo(ReservationStateRepository reservationStateRepository) {
        if (loadRequiredData && reservationStateRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.RESERVATIONSTATES, new TypeReference<List<ReservationState>>() {
            }, reservationStateRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle brands from a JSON file and saves them to a repository, but only if the loadRequiredData
     * flag is set to true.
     *
     * @param vehicleBrandlRepository This parameter is an instance of the VehicleBrandlRepository class, which is
     * responsible for interacting with the database to perform CRUD operations on the VehicleBrand entity.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleBrandsToRepo(VehicleBrandlRepository vehicleBrandlRepository) {
        if (loadRequiredData && vehicleBrandlRepository.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_BRAND, new TypeReference<List<VehicleBrand>>() {
            }, vehicleBrandlRepository::saveAll);
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle models from a JSON file, converts them to a list of vehicle models, and saves them to a
     * repository.
     *
     * @param vehicleModelService An instance of the VehicleModelService class, which is responsible for handling vehicle
     * model data and operations.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehicleModelsToRepo(VehicleModelService vehicleModelService) {
        if (loadRequiredData && vehicleModelService.findAll().isEmpty()) {
            return args -> readFromJson(DATA_TYPE.REQUIRED, JsonFileNames.VEHICLE_MODEL, new TypeReference<List<VehicleModelRequest>>() {
            }, vehicleModelRequests -> {
                try {
                    vehicleModelService.convertListOfVehicleModelRequestToListOfVehicleModelAndSave(vehicleModelRequests);
                } catch (BackendException e) {
                    loadingIndicator.stop(false);
                    throw new RuntimeException(e.getDescription());
                }
            });
        }
        return args -> {
        };
    }

    /**
     * The function reads member account data from a JSON file, converts it to a list of account objects, and saves them
     * using the registration service.
     *
     * @param registrationService An instance of the RegistrationService class, which is responsible for handling
     * registration-related operations.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readMemberAccountsToRepo(RegistrationService registrationService, DriverRepository driverRepository) {
        if ((loadDemoData || loadMockData) && driverRepository.findAll().isEmpty()) {

            DATA_TYPE dataType = loadDemoData ? DATA_TYPE.DEMO : DATA_TYPE.MOCK;
            return args -> readFromJson(dataType, JsonFileNames.MEMBER_ACCOUNTS, new TypeReference<List<RegisterRequest>>() {
            }, registerRequests -> {
                try {
                    registrationService.convertListOfRegisterRequestToListOfAccountAndSave(registerRequests, true);
                } catch (BackendException e) {
                    loadingIndicator.stop(false);
                    throw new RuntimeException(e.getDescription());
                }
            });
        }
        return args -> {
        };
    }

    /**
     * The function reads data from a JSON file and converts it into a list of account objects, which are then saved using
     * the registration service.
     *
     * @param registrationService An instance of the RegistrationService class, which is responsible for handling
     * registration-related operations.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readMAdminEmployeeAccountsToRepo(RegistrationService registrationService, EmployeeRepository employeeRepository) {
        if ((loadDemoData || loadMockData) && employeeRepository.findAll().isEmpty()) {

            DATA_TYPE dataType = loadDemoData ? DATA_TYPE.DEMO : DATA_TYPE.MOCK;
            return args -> readFromJson(dataType, JsonFileNames.ADMIN_EMPLOYEE_ACCOUNTS, new TypeReference<List<RegisterRequest>>() {
            }, registerRequests -> {
                try {
                    registrationService.convertListOfRegisterRequestToListOfAccountAndSave(registerRequests, false);
                } catch (BackendException e) {
                    loadingIndicator.stop(false);
                    throw new RuntimeException(e.getDescription());
                }
            });
        }
        return args -> {
        };
    }

    /**
     * The function reads vehicle data from a JSON file, converts it to a list of vehicle models, and saves them using a
     * vehicle service.
     *
     * @param vehicleService The vehicleService parameter is an instance of the VehicleService class. It is used to perform
     * operations related to vehicles, such as converting a list of vehicle requests to a list of vehicle models and saving
     * them.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readVehiclesToRepo(VehicleService vehicleService) {
        if ((loadDemoData || loadMockData) && vehicleService.findAll().isEmpty()) {

            DATA_TYPE dataType = loadDemoData ? DATA_TYPE.DEMO : DATA_TYPE.MOCK;
            return args -> readFromJson(dataType, JsonFileNames.VEHICLES, new TypeReference<List<VehicleRequest>>() {
            }, vehicleRequests -> {
                try {
                    vehicleService.convertListOfVehicleRequestToListOfVehicleModelAndSave(vehicleRequests);
                } catch (BackendException e) {
                    loadingIndicator.stop(false);
                    throw new RuntimeException(e);
                }
            });
        }
        return args -> {
        };
    }

    /**
     * The function reads reservations from a JSON file and converts them into a list of Reservation objects using a
     * ReservationService.
     *
     * @param reservationService The `reservationService` parameter is an instance of the `ReservationService` class. It is
     * used to perform operations related to reservations, such as converting a list of reservation requests to a list of
     * reservations.
     * @return The method is returning a CommandLineRunner object.
     */
    @Bean
    public CommandLineRunner readReservationsToRepo(ReservationService reservationService) {
        if ((loadDemoData || loadMockData) && reservationService.findAll().isEmpty()) {

            DATA_TYPE dataType = loadDemoData ? DATA_TYPE.DEMO : DATA_TYPE.MOCK;
            return args -> readFromJson(dataType, JsonFileNames.RESERVATIONS, new TypeReference<List<ReservationRequest>>() {
            }, reservationRequests -> {
                try {
                    reservationService.convertListOfReservationRequestToListOfReservation(reservationRequests);
                } catch (BackendException e) {
                    loadingIndicator.stop(false);
                    throw new RuntimeException(e.getDescription());
                }
            });
        }
        return args -> {
        };
    }

    /**
     * The function reads data from a JSON file based on the specified data type, performs an action on the data, and logs
     * the processing time.
     *
     * @param dataType The dataType parameter is an enum that specifies the type of data being read from the JSON file. It
     * can have three possible values: REQUIRED, MOCK, or DEMO.
     * @param fileName The name of the JSON file that you want to read from.
     * @param typeReference The `typeReference` parameter is a `TypeReference` object that represents the type of the data
     * you want to read from the JSON file. It is used by the `ObjectMapper` to correctly deserialize the JSON data into an
     * object of the specified type.
     * @param action The `action` parameter is a `Consumer` functional interface that represents an operation that accepts
     * a single input argument and returns no result. In this case, it is used to perform some action on the data read from
     * the JSON file. The `accept` method of the `Consumer` interface is called
     */
    private <T> void readFromJson(DATA_TYPE dataType, String fileName, TypeReference<T> typeReference, Consumer<T> action) {

        log.info("Start processing " + fileName + "...");

        Thread loadingThread = new Thread(loadingIndicator);
        loadingThread.start();

        String path = "";

        switch (dataType) {
            case REQUIRED -> path = "/json/" + fileName;
            case MOCK -> path = "/json/mock/" + fileName;
            case DEMO -> path = "/json/demo/" + fileName;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        try {
            LocalDateTime start = LocalDateTime.now();
            T data = objectMapper.readValue(inputStream, typeReference);
            action.accept(data);

            LocalDateTime end = LocalDateTime.now();
            loadingIndicator.stop(true);
            log.info("-> Processed " + fileName + " in " + Duration.between(start, end).toMillis() + " milliseconds!");

        } catch (IOException e) {
            log.info("Unable to process " + fileName + ": " + e.getMessage());
            loadingIndicator.stop(false);
        }
    }

    // The code below defines an enumeration called DATA_TYPE with three possible values: REQUIRED, DEMO, and MOCK.
    private enum DATA_TYPE {
        REQUIRED,
        DEMO,
        MOCK
    }
}