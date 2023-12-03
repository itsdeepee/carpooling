package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.Exceptions.RideRequest.DuplicateRideRequestException;
import org.example.Exceptions.Ride.RideNotFoundException;
import org.example.Exceptions.RideRequest.InvalidRideRequestCancellationException;
import org.example.Exceptions.RideRequest.RideRequestNotFoundException;
import org.example.Exceptions.User.AuthorizationException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.RideRequestDTOs.CreateRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.UpdateRideRequestDTO;
import org.example.Model.Entities.RideEntity;
import org.example.Model.Entities.RideRequestEntity;
import org.example.Model.Entities.UserEntity;
import org.example.Model.RideRequestStatus;
import org.example.Repository.RideRepository;
import org.example.Repository.RideRequestRepository;
import org.example.Repository.UserRepository;
import org.example.Service.Mappers.RideRequestMapper;
import org.example.Util.ConstraintNames;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    /**
     * Initiates a ride request based on the details provided in the CreateRideRequestDTO.
     * Retrieves the ride and user entities using the provided DTO,
     * creates a ride request entity, sets its details, saves it to the repository,
     * and updates the ride entity accordingly.
     * Returns a mapped DTO representing the ride request.
     *
     * @param createRideRequestDTO Details required to create a ride request.
     * @return A ResponseRideRequestDTO representing the requested ride.
     * @throws RuntimeException        if the ride is not found.
     * @throws UserNotFoundException   if the user does not exist.
     */
    @Transactional
    public ResponseRideRequestDTO requestRide(CreateRideRequestDTO createRideRequestDTO) {
        //TODO: if the DTO remains the same you have to check for null values
        RideEntity rideEntity = rideRepository.findByRideId(createRideRequestDTO.getRideId()).orElseThrow(() -> new RideNotFoundException("No ride found with id "+createRideRequestDTO.getRideId()));
        UserEntity passenger = userRepository.findById(createRideRequestDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User with id " + createRideRequestDTO.getUserId()+ " does not exist."));
        RideRequestEntity rideRequest = new RideRequestEntity();
        rideRequest.setRide(rideEntity);
        rideRequest.setPassenger(passenger);
        rideRequest.setStatus(RideRequestStatus.PENDING);
        try{
            RideRequestEntity createdEntity = rideRequestRepository.save(rideRequest);
            rideEntity.getRideRequestEntities().add(createdEntity);
            rideRepository.save(rideEntity);
        }catch(DataIntegrityViolationException ex){
            if(ex.getCause() instanceof ConstraintViolationException constraintViolationException){
                if(constraintViolationException.getConstraintName().equals(ConstraintNames.UNIQUE_RIDE_USER_CONSTRAINT)){
                  //TODO: add handler for this exception
                    throw new DuplicateRideRequestException("Duplicate ride request. A request for this ride by the user already exists.");
                }

            }
        }
        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);
    }

    @Transactional
    public List<ResponseRideRequestDTO> getRideRequestsForRide(CreateRideRequestDTO createRideRequestDTO, String status) {
        RideEntity rideEntity = rideRepository.findByRideId(createRideRequestDTO.getRideId()).orElseThrow(() -> new RideNotFoundException("Ride with given id does not exist."));
        if (!Objects.equals(rideEntity.getDriver().getId(), createRideRequestDTO.getUserId())) {
            throw new AuthorizationException("Trying to access ride requests information that are not managed by this user id.");

        }
        if(Objects.isNull(rideEntity.getRideRequestEntities()) || rideEntity.getRideRequestEntities().size()==0){
            return new ArrayList<>();
        }else{
            if (!Objects.isNull(status) && !status.isEmpty()) {
                RideRequestStatus rideRequestStatus = getRideRequestStatusFromString(status);
                Set<RideRequestEntity> rideRequests = rideEntity.getRideRequestEntities()
                        .stream()
                        .filter(request -> request.getStatus().equals(rideRequestStatus))
                        .collect(Collectors.toSet());
            }
            return rideEntity.getRideRequestEntities().stream()
                    .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();

        }


    }


    @Transactional
    public List<ResponseRideRequestDTO> getRequestsByStatus(Long userId, String status) {
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Uses with id " + userId + " does not exist."));
        RideRequestStatus rideRequestStatus = getRideRequestStatusFromString(status);

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
        //TODO: logic can be changed

        RideEntity rideEntity = rideRepository.findByRideId(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));

        RideRequestEntity rideRequest = new RideRequestEntity();
        if (Objects.equals(rideEntity.getDriver().getId(), driverId)) {
            Optional<RideRequestEntity> optionalRideRequest = rideEntity.getRideRequestEntities().stream()
                    .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRequestID(), requestId))
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
                    .filter(rideRequestEntity -> Objects.equals(rideRequestEntity.getRequestID(), requestId))
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
    public ResponseRideRequestDTO cancelRideRequest(UpdateRideRequestDTO updateRideRequestDTO) {
        //TODO: exception thrown here are not handled yet


        //just query for the ride request id and check everything from there
        RideRequestEntity rideRequestEntity=rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId())
                .orElseThrow(()->new RideRequestNotFoundException("Ride request does not exist."));


        if(!Objects.equals(rideRequestEntity.getPassenger().getUserId(),updateRideRequestDTO.getUserId())){
            throw new AuthorizationException("Looks like the provided user id does not correspond to the user that created this ride request.");

        }

        if (rideRequestEntity.getStatus().equals(RideRequestStatus.PENDING)) {
            rideRequestEntity.setStatus(RideRequestStatus.CANCELED);
            RideEntity rideEntity=rideRequestEntity.getRide();
            rideEntity.getRideRequestEntities().remove(rideRequestEntity);
            rideRepository.save(rideEntity);
        } else {
            throw new InvalidRideRequestCancellationException("Reason: Request has status: " + rideRequestEntity.getStatus().name() + ".");
        }


        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequestEntity);
    }

    @Transactional
    public boolean deleteRideRequest(Long requestId, Long userId) {
        //TODO: custom exceptions
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
        RideRequestEntity rideRequestEntity = rideRequestRepository.findByRequestID(requestId).orElseThrow(() -> new RuntimeException("Ride request with id " + requestId + " was not found"));
        if (!rideRequestEntity.getPassenger().getUserId().equals(userId)) {
            throw new RuntimeException("User did not make the request");
        }
        if (!rideRequestEntity.getStatus().equals(RideRequestStatus.CANCELED)) {
            throw new RuntimeException("Cannot delete request with status " + rideRequestEntity.getStatus().name());
        }

        rideRequestRepository.deleteByRequestID(requestId);
        Optional<RideRequestEntity> deletedEntity = rideRequestRepository.findByRequestID(requestId);
        return deletedEntity.isEmpty();

    }

    private RideRequestStatus getRideRequestStatusFromString(String status) {
        return switch (status.toLowerCase()) {
            case "confirmed" -> RideRequestStatus.CONFIRMED;
            case "pending" -> RideRequestStatus.PENDING;
            case "declined" -> RideRequestStatus.DECLINED;
            case "canceled" -> RideRequestStatus.CANCELED;
            default -> throw new IllegalStateException("Unexpected value: " + status.toLowerCase());
        };
    }
}
