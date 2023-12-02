package org.example.Service;


import jakarta.transaction.Transactional;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.PatchRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
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


    @Transactional
    public List<ResponseRideDTO> findActiveRidesFiltered(String startLocation, String endLocation) {

        List<RideEntity> activeRides = rideRepository.findByDateTimeOfRideAfter(LocalDateTime.now());
        if (!Objects.isNull(startLocation) && !Objects.isNull(endLocation)) {
            List<RideEntity> filteredRides = activeRides.stream().filter(rideEntity ->
                    rideEntity.getDepartureLocation().getFullPlaceName().contains(startLocation)
                            && rideEntity.getDestinationLocation().getFullPlaceName().contains(endLocation)


            ).toList();
            return filteredRides.stream()
                    .filter(rideEntity -> rideEntity.getRideStatus().equals(RideStatus.ACTIVE.name()))
                    .map(rideMapper::mapCreateRideEntitytoRideDTO).toList();
        }


        List<ResponseRideDTO> resultRideList =
                activeRides.stream()
                        .filter(rideEntity -> rideEntity.getRideStatus().equals(RideStatus.ACTIVE.name()))
                        .map(rideMapper::mapCreateRideEntitytoRideDTO).toList();


        return resultRideList;
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

    @Transactional
    public List<ResponseRideDTO> getRidesForUser(Long userId, String status) {
        DriverEntity driverEntity = driverRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not a driver"));

        Set<RideEntity> rideEntities = driverEntity.getRides();
        if (!Objects.isNull(status) && !status.isEmpty()) {
            RideStatus rideStatus = getStatusFromString(status);
            return rideEntities.stream().filter(rideEntity -> rideEntity.getRideStatus().equals(rideStatus.name()))
                    .map(rideMapper::mapCreateRideEntitytoRideDTO)
                    .toList();
        }


        return rideEntities.stream()
                .map(rideMapper::mapCreateRideEntitytoRideDTO)
                .toList();
    }

    @Transactional
    public ResponseRideDTO createRide(CreateRideDTO createRideDTO, Long userId) {
        DriverEntity driverEntity = driverRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User might not exist or is not registered as driver"));

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

    public ResponseRideDTO patchRide(PatchRideDTO patchRideDTOhRideDTo, Long userId, Long rideId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), userId)) {
            throw new RuntimeException("Not the driver of ride");
        }
        if (!Objects.equals(rideEntity.getRideStatus(), RideStatus.ACTIVE.name())) {
            throw new RuntimeException("Can update only active rides");
        }
        rideEntity.setAvailableSeats(patchRideDTOhRideDTo.getAvailableSeats());
        rideEntity.setCost(patchRideDTOhRideDTo.getCost());
        rideEntity.setAdditionalDetails(patchRideDTOhRideDTo.getAdditionalDetails());
        RideEntity modifiedRideEntity = rideRepository.save(rideEntity);
        return rideMapper.mapCreateRideEntitytoRideDTO(modifiedRideEntity);

    }

    public ResponseRideDTO updateRide(CreateRideDTO createRideDTO, Long userId, Long rideId) {

        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), userId)) {
            throw new RuntimeException("Not the driver of ride");
        }
        if (!Objects.equals(rideEntity.getRideStatus(), RideStatus.ACTIVE.name())) {
            throw new RuntimeException("Can update only active rides");
        }

        if (!Objects.isNull(rideEntity.getPassengers())) {
            if (rideEntity.getPassengers().size() > 0) {
                throw new RuntimeException("Cannot update ride because there are passengers for this ride. Please consider using PATCH ");
            }
        }

        //TODO: extract in ride mapper
        RideEntity updatedRideEntity = rideEntity;
        LocationEntity departureLocation = saveLocation(createRideDTO.getDepartureLocation());
        LocationEntity destinationLocation = saveLocation(createRideDTO.getDestinationLocation());
        updatedRideEntity.setDepartureLocation(departureLocation);
        updatedRideEntity.setDestinationLocation(destinationLocation);
        updatedRideEntity.setDateTimeOfRide(createRideDTO.getDateAndTimeOfRide());
        updatedRideEntity.setAvailableSeats(createRideDTO.getAvailableSeats());
        updatedRideEntity.setCost(createRideDTO.getCost());
        updatedRideEntity.setAdditionalDetails(createRideDTO.getAdditionalDetails());

        updatedRideEntity = rideRepository.save(updatedRideEntity);
        return rideMapper.mapCreateRideEntitytoRideDTO(updatedRideEntity);


    }


    @Transactional
    public ResponseRideDTO cancelRide(Long driverId, Long rideId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("No ride found"));
        if (!Objects.equals(rideEntity.getDriver().getId(), driverId)) {
            throw new RuntimeException("Not allowed to cancel ride. You can cancel only rides you created");
        }
        LocalDateTime timeOfRide = rideEntity.getDateTimeOfRide();
        Duration timeLeftToRide = Duration.between(LocalDateTime.now(), timeOfRide);
        if (timeLeftToRide.toHours() < 24) {
            if (rideEntity.getPassengers().size() > 0) {
                throw new RuntimeException("Not allowed to cancel ride. Ride cannot be cancel in less than 24 hours before ride if it has passengers");
            }
        }

        //TODO: notification that the ride was deleted.
        Set<RideRequestEntity> rideRequestEntities = rideEntity.getRideRequestEntities();
        for (RideRequestEntity rideRequest : rideRequestEntities) {
            rideRequest.setStatus(RideRequestStatus.DECLINED);
        }
        rideEntity.setRideStatus(RideStatus.CANCELED.name());

        rideEntity.setPassengers(null);


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
