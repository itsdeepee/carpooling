package org.example.Service;


import jakarta.transaction.Transactional;
import org.example.Exceptions.User.DuplicateEmailException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.LocationEntity;
import org.example.Model.Entities.RideEntity;
import org.example.Repository.DriverRepository;
import org.example.Repository.LocationRepository;
import org.example.Repository.RideRepository;
import org.example.Service.Mappers.LocationMapper;
import org.example.Service.Mappers.RideMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    private final DriverRepository driverRepository;

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public RideService(RideRepository rideRepository,
                       RideMapper rideMapper,
                       DriverRepository driverRepository,

                       LocationRepository locationRepository,
                       LocationMapper locationMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper=rideMapper;
        this.driverRepository = driverRepository;
        this.locationRepository=locationRepository;
        this.locationMapper=locationMapper;
    }

//    public List<ResponseRideDTO> getAllRides(){
//        return rideRepository.findAll();
//    }


    @Transactional
    public ResponseRideDTO createRide(CreateRideDTO createRideDTO, Long userId) {
        DriverEntity driverEntity = driverRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User might not exist or is not registered as driver"));
        // save locations in database if new.
        LocationEntity departureLocation=saveLocation(createRideDTO.getDepartureLocation());
        LocationEntity destinationLocation=saveLocation(createRideDTO.getDestinationLocation());
       //save new ride
        RideEntity rideEntity=rideMapper.mapCreateRideDTOtoRideEntity(createRideDTO,driverEntity);
        rideEntity.setDepartureLocation(departureLocation);
        rideEntity.setDestinationLocation(destinationLocation);
        RideEntity savedRideEntity=rideRepository.save(rideEntity);

        //update driver info
        Set<LocationEntity> searchedLocations=new HashSet<>();
        searchedLocations.add(departureLocation);
        searchedLocations.add(destinationLocation);
        driverEntity.setRecentAddresses(searchedLocations);

        driverEntity.getRides().add(savedRideEntity);
        driverRepository.save(driverEntity);

        return rideMapper.mapCreateRideEntitytoRideDTO(rideEntity);
    }

    private LocationEntity saveLocation(LocationDTO locationDTO){
        Optional<LocationEntity> resultLocationEntity =locationRepository.findByFullPlaceName(locationDTO.getFullPlaceName());
        if(resultLocationEntity.isPresent()){
            return resultLocationEntity.get();
        }
        LocationEntity locationEntity=locationMapper.mapLocationDTOtoLocationEntity(locationDTO);
        locationEntity=locationRepository.save(locationEntity);
        return locationEntity;
    }




}
