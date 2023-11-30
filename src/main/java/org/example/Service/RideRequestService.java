package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.Exceptions.User.UserNotFoundException;
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
    public ResponseRideRequestDTO requestRide(Long rideId, Long userId) {
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("No ride found"));
        UserEntity passenger = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " does not exist."));
        RideRequestEntity rideRequest = new RideRequestEntity();
        rideRequest.setRide(rideEntity);
        rideRequest.setPassenger(passenger);
        rideRequest.setStatus(RideRequestStatus.PENDING);
        rideRequest = rideRequestRepository.save(rideRequest);
        rideEntity.getRideRequestEntities().add(rideRequest);
        rideRepository.save(rideEntity);

        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);

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
    public List<ResponseRideRequestDTO> getRequestsByStatus(Long userId, String status) {
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Uses with id " + userId + " does not exist."));
        RideRequestStatus rideRequestStatus = switch (status.toLowerCase()) {
            case "confirmed" -> RideRequestStatus.CONFIRMED;
            case "pending" -> RideRequestStatus.PENDING;
            case "declined" -> RideRequestStatus.DECLINED;
            case "canceled" -> RideRequestStatus.CANCELED;
            default -> throw new IllegalStateException("Unexpected value: " + status.toLowerCase());
        };

        List<RideRequestEntity> rideRequestEntities = rideRequestRepository.findByPassengerAndStatus(userEntity, rideRequestStatus);

        if (rideRequestEntities.isEmpty()) {
            return responseRideRequestDTOS;
        }
        responseRideRequestDTOS = rideRequestEntities.stream()
                .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();
        return responseRideRequestDTOS;

    }

    @Transactional
    public List<ResponseRideRequestDTO> getAllRequests(Long userId) {
        //TODO: custom exceptions
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Uses with id " + userId + " does not exist."));
        List<RideRequestEntity> rideRequestEntities = rideRequestRepository.findByPassenger(userEntity);

        if (rideRequestEntities.isEmpty()) {
            return responseRideRequestDTOS;
        }
        responseRideRequestDTOS = rideRequestEntities.stream()
                .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();
        return responseRideRequestDTOS;

    }

    @Transactional
    public ResponseRideRequestDTO acceptRideRequest(Long driverId, Long rideId, Long requestId) {
        //TODO: custom exceptions
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));

        RideRequestEntity rideRequest = new RideRequestEntity();
        if (Objects.equals(rideEntity.getDriver().getId(), driverId)) {
            Optional<RideRequestEntity> optionalRideRequest = rideEntity.getRideRequestEntities().stream()
                    .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRideRequestID(), requestId))
                    .findFirst();

            rideRequest = optionalRideRequest.orElseThrow(() -> new RuntimeException("Request does not exist for this ride"));

            if (rideRequest.getStatus().name().equalsIgnoreCase("pending")) {
                rideRequest.setStatus(RideRequestStatus.CONFIRMED);
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
        //TODO: custom exceptions
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

    @Transactional
    public ResponseRideRequestDTO cancelRideRequest(Long userId, Long rideId, Long requestId) {
        //TODO: custom exceptions
        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        RideRequestEntity rideRequest;

        Optional<RideRequestEntity> optionalRideRequest = rideEntity.getRideRequestEntities().stream()
                .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRideRequestID(), requestId))
                .findFirst();

        rideRequest = optionalRideRequest.orElseThrow(() -> new RuntimeException("Request does not exist for this ride"));

        if (Objects.equals(rideRequest.getPassenger().getUserId(), userId)) {
            throw new RuntimeException("Request was not made by this user");
        }

        if (rideRequest.getStatus().equals(RideRequestStatus.PENDING)) {
            rideRequest.setStatus(RideRequestStatus.CANCELED);
            rideEntity.getRideRequestEntities().remove(rideRequest);
            rideRepository.save(rideEntity);
        } else {
            throw new RuntimeException("Request cannot be canceled. Reason: Request has status: " + rideRequest.getStatus().name() + ".");
        }


        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);
    }

    @Transactional
    public boolean deleteRideRequest(Long requestId, Long userId) {
        //TODO: custom exceptions
        List<ResponseRideRequestDTO> responseRideRequestDTOS=new ArrayList<>();
        RideRequestEntity rideRequestEntity=rideRequestRepository.findByRideRequestID(requestId).orElseThrow(()->new RuntimeException("Ride request with id "+requestId+" was not found"));
        if(!rideRequestEntity.getPassenger().getUserId().equals(userId)){
            throw new RuntimeException("User did not make the request");
        }
        if(!rideRequestEntity.getStatus().equals(RideRequestStatus.CANCELED)){
            throw new RuntimeException("Cannot delete request with status "+rideRequestEntity.getStatus().name());
        }

        rideRequestRepository.deleteByRequestId(requestId);
         Optional<RideRequestEntity> deletedEntity=rideRequestRepository.findByRideRequestID(requestId);
        return deletedEntity.isEmpty();

    }
}
