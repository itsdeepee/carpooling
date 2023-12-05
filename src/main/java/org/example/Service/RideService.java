package org.example.Service;


import jakarta.transaction.Transactional;
import org.example.Exceptions.Driver.DriverNotFoundException;
import org.example.Exceptions.Ride.CustomExceptions.RideNotFoundException;
import org.example.Exceptions.Ride.CustomExceptions.InactiveRideUpdateException;
import org.example.Exceptions.User.AccessDeniedException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.DTOs.RideDTOs.*;
import org.example.Model.Entities.*;
import org.example.Model.RideRequestStatus;
import org.example.Repository.DriverRepository;
import org.example.Repository.LocationRepository;
import org.example.Repository.RideRepository;
import org.example.Service.Mappers.LocationMapper;
import org.example.Service.Mappers.RideMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    private final DriverRepository driverRepository;

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public RideService(RideRepository rideRepository,
                       RideMapper rideMapper,
                       DriverRepository driverRepository,
                       LocationRepository locationRepository,
                       LocationMapper locationMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
        this.driverRepository = driverRepository;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    /**
     * Retrieves a list of all rides optionally filtered by start and end locations.
     * If start or end locations are provided, filters rides that contain the provided locations.
     * If no locations are provided, retrieves all  rides.
     *
     * @param startLocation Start location to filter the rides (can be null or empty).
     * @param endLocation   End location to filter the rides (can be null or empty).
     * @return A list of ResponseRideDTO objects representing active rides, possibly filtered by locations.
     */
    @Transactional
    public List<ResponseRideDTO> findAllRidesFiltered(String startLocation, String endLocation) {
        String start = (startLocation != null) ? startLocation : "";
        String end = (endLocation != null) ? endLocation : "";
        List<RideEntity> rides = rideRepository.findAll();
        if (!start.isEmpty() || !end.isEmpty()) {
            List<RideEntity> filteredRides = rides.stream()
                    .filter(rideEntity ->
                            rideEntity.getDepartureLocation().getFullPlaceName().contains(start) ||
                                    rideEntity.getDestinationLocation().getFullPlaceName().contains(end)
                    ).toList();
            filteredRides.forEach(System.out::println);
            return filteredRides.stream()
                    .map(rideMapper::mapCreateRideEntitytoRideDTO).toList();
        }
        return rides.stream().map(rideMapper::mapCreateRideEntitytoRideDTO).toList();
    }

    @Transactional
    public List<ResponseRideDTO> getRidesHistory(Long userId) {

        DriverEntity driverEntity = driverRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not a driver"));

        Set<RideEntity> rideEntities = driverEntity.getRides();
        List<ResponseRideDTO> resultRideList = rideEntities.stream().filter(rideEntity -> !rideEntity.getRideStatus().equals(RideStatus.ACTIVE.name()))
                .map(rideMapper::mapCreateRideEntitytoRideDTO)
                .toList();


        resultRideList.forEach(System.out::println);
        return resultRideList;
    }

    /**
     * Retrieves all rides created by a specific driver, optionally filtered by status.
     *
     * @param userId The unique identifier of the driver.
     * @param status The status to filter the rides by (optional).
     * @return A list of ResponseRideDTO representing rides created by the driver.
     * @throws DriverNotFoundException When the driver is not found based on the provided user ID.
     */
    @Transactional
    public List<ResponseRideDTO> getAllRidesCreatedByDriver(Long userId, String status) {
        DriverEntity driverEntity = driverRepository.findById(userId).orElseThrow(() -> new DriverNotFoundException("Driver not found for user id: "));

        Set<RideEntity> rideEntities = driverEntity.getRides();
        if (!Objects.isNull(status) && !status.isEmpty()) {
            RideStatus rideStatus = getStatusFromString(status);
            return rideEntities.stream()
                    .filter(rideEntity -> rideEntity.getRideStatus().equals(rideStatus.name()))
                    .map(rideMapper::mapCreateRideEntitytoRideDTO)
                    .collect(Collectors.toList());
        }


        return rideEntities.stream()
                .map(rideMapper::mapCreateRideEntitytoRideDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseRideDTO createRide(CreateRideDTO createRideDTO) {
        DriverEntity driverEntity = driverRepository.findById(createRideDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User might not exist or is not registered as driver"));

        LocationEntity departureLocation = saveLocation(createRideDTO.getDepartureLocation());
        LocationEntity destinationLocation = saveLocation(createRideDTO.getDestinationLocation());

        RideEntity rideEntity = rideMapper.mapCreateRideDTOtoRideEntity(createRideDTO, driverEntity);
        rideEntity.setDepartureLocation(departureLocation);
        rideEntity.setDestinationLocation(destinationLocation);
        rideEntity.setRideStatus(RideStatus.ACTIVE.name());
        RideEntity savedRideEntity = rideRepository.save(rideEntity);

        Set<LocationEntity> searchedLocations = new HashSet<>();
        searchedLocations.add(departureLocation);
        searchedLocations.add(destinationLocation);
        driverEntity.setRecentAddresses(searchedLocations);

        driverEntity.getRides().add(savedRideEntity);
        driverRepository.save(driverEntity);

        return rideMapper.mapCreateRideEntitytoRideDTO(rideEntity);
    }

    /**
     * Updates the specified ride entity partially based on the provided PatchRideDTO.
     * <p>
     * 1. Retrieves the ride entity from the repository using the ride ID from the PatchRideDTO.
     * 2. Validates the user's permission by comparing the ride's driver ID with the user ID from the PatchRideDTO.
     * 3. Ensures that updates are allowed only for rides in an active status; otherwise, throws an exception.
     * 4. Updates the ride entity fields if corresponding values are provided and valid in the PatchRideDTO.
     * 5. Saves the modified ride entity back to the repository.
     *
     * @param patchRideDTO The PatchRideDTO containing fields to update for the ride entity.
     * @return The ResponseRideDTO representing the modified ride entity.
     * @throws RideNotFoundException       When the requested ride entity is not found in the repository.
     * @throws AccessDeniedException       When the user attempting the update is not the driver of the ride.
     * @throws InactiveRideUpdateException When updates are attempted on rides that are not in an active status.
     */
    public ResponseRideDTO patchRide(PatchRideDTO patchRideDTO) {
        RideEntity rideEntity = rideRepository.findByRideId(patchRideDTO.getRideId())
                .orElseThrow(() -> new RideNotFoundException("Ride not found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), patchRideDTO.getUserId())) {
            throw new AccessDeniedException("Not the driver");
        }
        if (!Objects.equals(rideEntity.getRideStatus(), RideStatus.ACTIVE.name())) {
            throw new InactiveRideUpdateException("Can update only active rides");
        }
        if (patchRideDTO.getAvailableSeats() > 0) {
            rideEntity.setAvailableSeats(patchRideDTO.getAvailableSeats());
        }

        if (patchRideDTO.getCost() > 0) {
            rideEntity.setCost(patchRideDTO.getCost());
        }

        if (patchRideDTO.getAdditionalDetails() != null && !patchRideDTO.getAdditionalDetails().isEmpty()) {
            rideEntity.setAdditionalDetails(patchRideDTO.getAdditionalDetails());
        }

        RideEntity modifiedRideEntity = rideRepository.save(rideEntity);
        return rideMapper.mapCreateRideEntitytoRideDTO(modifiedRideEntity);

    }

    public ResponseRideDTO updateRide(UpdateRideDTO updateRideDTO) {

        RideEntity rideEntity = rideRepository.findByRideId(updateRideDTO.getRideId()).orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), updateRideDTO.getUserId())) {
            throw new AccessDeniedException("No access rights to modify this ride. Not the same user");
        }
        if (!Objects.equals(rideEntity.getRideStatus(), RideStatus.ACTIVE.name())) {
            throw new InactiveRideUpdateException("Can only update active rides");
        }

        //TODO: modify to custom exceptions
        if (!Objects.isNull(rideEntity.getPassengers())) {
            if (rideEntity.getPassengers().size() > 0) {
                throw new RuntimeException("Cannot update ride because there are passengers for this ride. Please consider using PATCH ");
            }
        }
        if(Objects.isNull(updateRideDTO.getRide())){
            throw new RuntimeException("Object to update is null");
        }

        //TODO: extract in ride mapper
        RideEntity updatedRideEntity = rideEntity;
        CreateRideDTO updatedRide=updateRideDTO.getRide();
        LocationEntity departureLocation = saveLocation(updatedRide.getDepartureLocation());
        LocationEntity destinationLocation = saveLocation(updatedRide.getDestinationLocation());
        updatedRideEntity.setDepartureLocation(departureLocation);
        updatedRideEntity.setDestinationLocation(destinationLocation);
        updatedRideEntity.setDateTimeOfRide(updatedRide.getDateAndTimeOfRide());
        updatedRideEntity.setAvailableSeats(updatedRide.getAvailableSeats());
        updatedRideEntity.setCost(updatedRide.getCost());
        updatedRideEntity.setAdditionalDetails(updatedRide.getAdditionalDetails());

        updatedRideEntity = rideRepository.save(updatedRideEntity);
        return rideMapper.mapCreateRideEntitytoRideDTO(updatedRideEntity);


    }


    @Transactional
    public ResponseRideDTO cancelRide(CancelRideDTO cancelRideDTO) {
        RideEntity rideEntity = rideRepository.findByRideId(cancelRideDTO.getRideId()).orElseThrow(() -> new RideNotFoundException("No ride found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), cancelRideDTO.getUserId())) {
            throw new AccessDeniedException("No access right to cancel rides. User did not created this ride");
        }
        LocalDateTime timeOfRide = rideEntity.getDateTimeOfRide();
        Duration timeLeftToRide = Duration.between(LocalDateTime.now(), timeOfRide);
        if (timeLeftToRide.toHours() < 24) {
            if (!Objects.isNull(rideEntity.getPassengers())) {
                if (!rideEntity.getPassengers().isEmpty()){
                    throw new RuntimeException("Not allowed to cancel ride. Ride cannot be cancel in less than 24 hours before ride if it has passengers");
                }
            }
        }

        //TODO: notification that the ride was deleted.
        Set<RideRequestEntity> rideRequestEntities = rideEntity.getRideRequestEntities();
        for (RideRequestEntity rideRequest : rideRequestEntities) {
            rideRequest.setStatus(RideRequestStatus.DECLINED);
        }
        rideEntity.setRideStatus(RideStatus.CANCELED.name());
        rideEntity.setPassengers(new HashSet<>());
        RideEntity modifiedRide = rideRepository.save(rideEntity);
        return rideMapper.mapCreateRideEntitytoRideDTO(modifiedRide);


    }

    private LocationEntity saveLocation(LocationDTO locationDTO) {
        Optional<LocationEntity> resultLocationEntity = locationRepository.findByFullPlaceName(locationDTO.getFullPlaceName());
        if (resultLocationEntity.isPresent()) {
            return resultLocationEntity.get();
        }
        LocationEntity locationEntity = locationMapper.mapLocationDTOtoLocationEntity(locationDTO);
        locationEntity = locationRepository.save(locationEntity);
        return locationEntity;
    }

    private RideStatus getStatusFromString(String status) {
        return switch (status) {
            case "active" -> RideStatus.ACTIVE;
            case "canceled" -> RideStatus.CANCELED;
            case "completed" -> RideStatus.COMPLETED;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }


}
