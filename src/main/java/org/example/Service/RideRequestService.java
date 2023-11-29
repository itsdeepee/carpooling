package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.RideRequestDTOs.CreateRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Model.Entities.RideEntity;
import org.example.Model.Entities.RideRequestEntity;
import org.example.Model.Entities.UserEntity;
import org.example.Model.RideRequestStatus;
import org.example.Repository.RideRepository;
import org.example.Repository.RideRequestRepository;
import org.example.Repository.UserRepository;
import org.example.Service.Mappers.RideRequestMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RideRequestService {
    private final RideRequestRepository rideRequestRepository;
    private final RideRequestMapper rideRequestMapper;
    public final RideRepository rideRepository;
    private final UserRepository userRepository;


    public RideRequestService(RideRepository rideRepository,
                              UserRepository userRepository,
                              RideRequestRepository rideRequestRepository,
                              RideRequestMapper rideRequestMapper) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.rideRequestMapper = rideRequestMapper;
    }

    @Transactional
    public void requestRide(Long rideId, CreateRideRequestDTO createRideRequestDTO) {

        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("No ride found"));

        UserEntity passenger = userRepository.findById(createRideRequestDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User with id " + createRideRequestDTO.getUserId() + " does not exist."));

        RideRequestEntity rideRequest = new RideRequestEntity();
        rideRequest.setRide(rideEntity);
        rideRequest.setPassenger(passenger);
        rideRequest.setStatus(RideRequestStatus.PENDING);


        rideRequest = rideRequestRepository.save(rideRequest);
        rideEntity.getRideRequestEntities().add(rideRequest);
        rideRepository.save(rideEntity);


        System.out.println(rideEntity);
    }

    @Transactional
    public List<ResponseRideRequestDTO> getRideRequestsForRide(Long rideId, Long userId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Not found"));

        if (!Objects.equals(rideEntity.getDriver().getId(), userId)) {
            throw new RuntimeException("Not authorized to access this information");
        }
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();

        Set<RideRequestEntity> rideRequests = rideEntity.getRideRequestEntities();

        if (!Objects.isNull(rideRequests)) {
            responseRideRequestDTOS = rideRequests.stream()
                    .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();

            return responseRideRequestDTOS;
        }

        return responseRideRequestDTOS;

    }

    @Transactional
    public ResponseRideRequestDTO acceptRideRequest(Long driverId, Long rideId, Long requestId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));

        RideRequestEntity rideRequest = new RideRequestEntity();
        if (Objects.equals(rideEntity.getDriver().getId(), driverId)) {
            Optional<RideRequestEntity> optionalRideRequest = rideEntity.getRideRequestEntities().stream()
                    .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRideRequestID(), requestId))
                    .findFirst();

            rideRequest = optionalRideRequest.orElseThrow(() -> new RuntimeException("Request does not exist for this ride"));

            if (rideRequest.getStatus().name().equalsIgnoreCase("pending")) {
                rideRequest.setStatus(RideRequestStatus.CONFIRMED);
                //   rideRequestRepository.save(rideRequest);
                rideEntity.getPassengers().add(rideRequest.getPassenger());
                rideEntity.setAvailableSeats(rideEntity.getAvailableSeats() - 1);
                rideEntity.getRideRequestEntities().remove(rideRequest);
                rideRepository.save(rideEntity);
            } else {
                throw new RuntimeException("Request cannot be confirmed. Reason: Request has status: " + rideRequest.getStatus().name() + ".");
            }

        } else {
            throw new RuntimeException("Driver does not own this ride");
        }
        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);
    }

    @Transactional
    public ResponseRideRequestDTO declineRideRequest(Long driverId, Long rideId, Long requestId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        RideRequestEntity rideRequest = new RideRequestEntity();
        if (Objects.equals(rideEntity.getDriver().getId(), driverId)) {
            Optional<RideRequestEntity> optionalRideRequest = rideEntity.getRideRequestEntities().stream()
                    .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRideRequestID(), requestId))
                    .findFirst();

            rideRequest = optionalRideRequest.orElseThrow(() -> new RuntimeException("Request does not exist for this ride"));

            if (rideRequest.getStatus().name().equalsIgnoreCase("pending")) {
                rideRequest.setStatus(RideRequestStatus.DECLINED);
                rideEntity.getRideRequestEntities().remove(rideRequest);
                rideRepository.save(rideEntity);
            } else {
                throw new RuntimeException("Request cannot be confirmed. Reason: Request has status: " + rideRequest.getStatus().name() + ".");
            }

        } else {
            throw new RuntimeException("Driver does not own this ride");
        }
        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);
    }
}
