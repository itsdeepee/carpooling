package org.example.Service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.example.Exceptions.RideRequest.CustomExceptions.*;
import org.example.Exceptions.Ride.CustomExceptions.RideNotFoundException;
import org.example.Exceptions.User.AccessDeniedException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RideRequestService {
    private final RideRequestRepository rideRequestRepository;
    private final RideRequestMapper rideRequestMapper;
    public final RideRepository rideRepository;
    private final UserRepository userRepository;


    @Autowired
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
     * @throws RuntimeException      if the ride is not found.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    public ResponseRideRequestDTO requestRide(CreateRideRequestDTO createRideRequestDTO) {
        //TODO: if the DTO remains the same you have to check for null values
        RideEntity rideEntity = rideRepository.findByRideId(createRideRequestDTO.getRideId()).orElseThrow(() -> new RideNotFoundException("No ride found with id " + createRideRequestDTO.getRideId()));
        UserEntity passenger = userRepository.findById(createRideRequestDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User with id " + createRideRequestDTO.getUserId() + " does not exist."));
        if (rideEntity.getAvailableSeats() <= 0) {
            throw new RequestsForFullRideException("Request cannot be created because there are no more available seats");
        }
        RideRequestEntity rideRequest = new RideRequestEntity();
        rideRequest.setRide(rideEntity);
        rideRequest.setPassenger(passenger);
        rideRequest.setStatus(RideRequestStatus.PENDING);
        try {
            RideRequestEntity createdEntity = rideRequestRepository.save(rideRequest);
            rideEntity.getRideRequestEntities().add(createdEntity);
            rideRepository.save(rideEntity);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
                if (constraintViolationException.getConstraintName().equals(ConstraintNames.UNIQUE_RIDE_USER_CONSTRAINT)) {
                    throw new DuplicateRideRequestException("This user has already made a request for this ride");
                }

            }
        }
        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequest);
    }

    /**
     * Retrieves ride requests received for a specific ride made by the driver with the given userId.
     *
     * @param createRideRequestDTO Details of the ride request.
     * @param status               Optional parameter to filter ride requests by status.
     * @return List of ResponseRideRequestDTO containing ride requests received for the ride made by the driver.
     * If status is specified, returns filtered ride requests; otherwise, returns all ride requests.
     * @throws RideNotFoundException If the requested ride with the given ID does not exist.
     * @throws AccessDeniedException If the user attempting to access the ride requests is not the driver of the ride.
     */
    @Transactional
    public List<ResponseRideRequestDTO> getRideRequestsReceivedForRideByDriver(CreateRideRequestDTO createRideRequestDTO, String status) {
        RideEntity rideEntity = rideRepository.findByRideId(createRideRequestDTO.getRideId()).orElseThrow(() -> new RideNotFoundException("Ride with given id does not exist."));
        if (!Objects.equals(rideEntity.getDriver().getId(), createRideRequestDTO.getUserId())) {
            throw new AccessDeniedException("You do not have access rights to view this ride requests.");
        }

        if (Objects.isNull(rideEntity.getRideRequestEntities()) || rideEntity.getRideRequestEntities().isEmpty()) {
            return new ArrayList<>();
        }
        if (!Objects.isNull(status) && !status.isEmpty()) {
            RideRequestStatus rideRequestStatus = getRideRequestStatusFromString(status);
            return rideEntity.getRideRequestEntities()
                    .stream()
                    .filter(request -> request.getStatus().equals(rideRequestStatus))
                    .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO)
                    .toList();
        }
        return rideEntity.getRideRequestEntities().stream()
                .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();


    }

    /**
     * Retrieves ride requests sent by a specific user, potentially filtered by request status.
     *
     * @param createRideRequestDTO Details of the ride request containing the user ID.
     * @param status               Optional parameter to filter ride requests by status.
     * @return List of ResponseRideRequestDTO containing ride requests sent by the user.
     * If status is specified, returns filtered ride requests; otherwise, returns all ride requests sent by the user.
     * @throws UserNotFoundException If the requested user with the given ID does not exist.
     */
    @Transactional
    public List<ResponseRideRequestDTO> getRideRequestsSentByUserWithOptionalFilter(CreateRideRequestDTO createRideRequestDTO, String status) {
        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
        UserEntity userEntity = userRepository.findById(createRideRequestDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("Uses with id " + createRideRequestDTO.getUserId() + " does not exist."));


        List<RideRequestEntity> rideRequestEntities;
        if (StringUtils.isEmpty(status)) {
            RideRequestStatus rideRequestStatus = getRideRequestStatusFromString(status);
            rideRequestEntities = rideRequestRepository.findByPassengerAndStatus(userEntity, rideRequestStatus);
        } else {
            rideRequestEntities = rideRequestRepository.findByPassenger(userEntity);
        }

        if (Objects.isNull(rideRequestEntities) || rideRequestEntities.isEmpty()) {
            return responseRideRequestDTOS;
        }
        return rideRequestEntities.stream()
                .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();


    }
//TODO: delete below method after testing

//    @Transactional
//    public List<ResponseRideRequestDTO> getAllRequests(Long userId) {
//
//        List<ResponseRideRequestDTO> responseRideRequestDTOS = new ArrayList<>();
//        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Uses with id " + userId + " does not exist."));
//        List<RideRequestEntity> rideRequestEntities = rideRequestRepository.findByPassenger(userEntity);
//
//
//        if (rideRequestEntities.isEmpty()) {
//            return responseRideRequestDTOS;
//        }
//        responseRideRequestDTOS = rideRequestEntities.stream()
//                .map(rideRequestMapper::mapRideRequestEntityToResponseRideRequestDTO).toList();
//        return responseRideRequestDTOS;
//
//    }

    /**
     * Accepts a ride request, updating its status to CONFIRMED if it's in a pending state,
     * adds the passenger to the ride, and adjusts available seats.
     *
     * @param updateRideRequestDTO The updated ride request details containing the request ID and user ID.
     * @return ResponseRideRequestDTO representing the accepted ride request details.
     * @throws RideRequestNotFoundException      If the requested ride request ID does not exist.
     * @throws AccessDeniedException             If the user attempting to accept the ride request is not the driver.
     * @throws RequestsForFullRideException      If the ride seats are fully occupied.
     * @throws InvalidRideRequestAcceptException If the ride request status is not pending.
     */
    @Transactional
    public ResponseRideRequestDTO acceptRideRequest(UpdateRideRequestDTO updateRideRequestDTO) {
        RideRequestEntity rideRequestEntity = rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId())
                .orElseThrow(() -> new RideRequestNotFoundException("Unable to accept ride. Ride request with id " + updateRideRequestDTO.getRequestId() + " does not exist."));

        RideEntity rideEntity = rideRequestEntity.getRide();
        if (!Objects.equals(rideEntity.getDriver().getId(), updateRideRequestDTO.getUserId())) {
            throw new AccessDeniedException("You do not have access rights to manage this ride request.");
        }

        if (!rideRequestEntity.getStatus().equals(RideRequestStatus.PENDING)) {
            throw new InvalidRideRequestAcceptException("Request cannot be accepted. Current status: " + rideRequestEntity.getStatus() + ". Expected status: PENDING");
        }

        if (rideEntity.getAvailableSeats() == 0) {
            throw new RequestsForFullRideException("Ride seats are fully occupied.");
        }
        rideRequestEntity.setStatus(RideRequestStatus.CONFIRMED);
        rideEntity.getPassengers().add(rideRequestEntity.getPassenger());
        rideEntity.setAvailableSeats(rideEntity.getAvailableSeats() - 1);
        rideEntity.getRideRequestEntities().remove(rideRequestEntity);
        rideRepository.save(rideEntity);


        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequestEntity);
    }

    /**
     * Declines a ride request, updating its status to DECLINED if it's in a pending state.
     * Removes the declined request from the associated ride.
     *
     * @param updateRideRequestDTO The updated ride request details containing the request ID and user ID.
     * @return ResponseRideRequestDTO representing the declined ride request details.
     * @throws RideRequestNotFoundException       If the requested ride request ID does not exist.
     * @throws AccessDeniedException              If the user attempting to decline the ride request is not the driver.
     * @throws InvalidRideRequestDeclineException If the ride request status is not pending.
     */
    @Transactional
    public ResponseRideRequestDTO declineRideRequest(UpdateRideRequestDTO updateRideRequestDTO) {
        RideRequestEntity rideRequestEntity = rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId())
                .orElseThrow(() -> new RideRequestNotFoundException("Unable to accept ride. Ride request with id " + updateRideRequestDTO.getRequestId() + " does not exist."));

        RideEntity rideEntity = rideRequestEntity.getRide();
        if (!Objects.equals(rideEntity.getDriver().getId(), updateRideRequestDTO.getUserId())) {
            throw new AccessDeniedException("You do not have access rights to manage this ride request.");
        }
        if (!rideRequestEntity.getStatus().equals(RideRequestStatus.PENDING)) {
            throw new InvalidRideRequestDeclineException("Request cannot be declined. Current status: " + rideRequestEntity.getStatus() + ". Expected status: PENDING");
        }
        rideRequestEntity.setStatus(RideRequestStatus.DECLINED);
        rideEntity.getRideRequestEntities().remove(rideRequestEntity);
        rideRepository.save(rideEntity);
        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequestEntity);
    }


//TODO: delete below method after testing

//    @Transactional
//    public ResponseRideRequestDTO cancelRideRequest(UpdateRideRequestDTO updateRideRequestDTO) {
//        RideRequestEntity rideRequestEntity = rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId())
//                .orElseThrow(() -> new RideRequestNotFoundException("Unable to cancel ride. Ride request with id " + updateRideRequestDTO.getRequestId() + " does not exist."));
//
//        if (!Objects.equals(rideRequestEntity.getPassenger().getUserId(), updateRideRequestDTO.getUserId())) {
//            throw new AuthorizationException("Looks like the provided user id does not correspond to the user that created this ride request.");
//        }
//        if (rideRequestEntity.getStatus().equals(RideRequestStatus.PENDING)) {
//            rideRequestEntity.setStatus(RideRequestStatus.CANCELED);
//            RideEntity rideEntity = rideRequestEntity.getRide();
//            rideEntity.getRideRequestEntities().remove(rideRequestEntity);
//            rideRepository.save(rideEntity);
//        } else {
//            throw new InvalidRideRequestDeleteException("Reason: Request has status: " + rideRequestEntity.getStatus().name() + ". The expected status name is \"pending\"");
//        }
//        return rideRequestMapper.mapRideRequestEntityToResponseRideRequestDTO(rideRequestEntity);
//    }

    /**
     * Deletes a ride request if it's in a pending state and the user attempting the deletion is the request creator.
     *
     * @param updateRideRequestDTO The updated ride request details containing the request ID and user ID.
     * @return True if the ride request is deleted successfully; otherwise, false.
     * @throws RideRequestNotFoundException      If the requested ride request ID does not exist.
     * @throws AccessDeniedException             If the user attempting to delete the ride request is not the request creator.
     * @throws InvalidRideRequestDeleteException If the ride request status is not pending.
     */
    @Transactional
    public boolean deleteRideRequest(UpdateRideRequestDTO updateRideRequestDTO) {
        RideRequestEntity rideRequestEntity = rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId())
                .orElseThrow(() -> new RideRequestNotFoundException("Unable to cancel ride. Ride request with id " + updateRideRequestDTO.getRequestId() + " does not exist."));

        if (!Objects.equals(rideRequestEntity.getPassenger().getUserId(), updateRideRequestDTO.getUserId())) {
            throw new AccessDeniedException("You do not have access rights to delete this ride request. Ride request can be deleted by the user that created it.");
        }

        if (!rideRequestEntity.getStatus().equals(RideRequestStatus.PENDING)) {
            throw new InvalidRideRequestDeleteException("Request cannot be deleted. Current status: " + rideRequestEntity.getStatus() + ". Expected status: PENDING");
        }
        RideEntity rideEntity = rideRequestEntity.getRide();
        rideEntity.getRideRequestEntities().remove(rideRequestEntity);
        rideRepository.save(rideEntity);
        rideRequestRepository.deleteByRequestID(rideRequestEntity.getRequestID());
        Optional<RideRequestEntity> deletedEntity = rideRequestRepository.findByRequestID(updateRideRequestDTO.getRequestId());
        return deletedEntity.isEmpty();
    }

    /**
     * Converts a string representation of a ride request status to its corresponding RideRequestStatus enum value.
     *
     * @param status The string representation of the ride request status in lowercase.
     * @return The RideRequestStatus enum value corresponding to the provided status string.
     * @throws IllegalStatusException If the status string doesn't match any expected values.
     */
    private RideRequestStatus getRideRequestStatusFromString(String status) {
        return switch (status.trim().toLowerCase()) {
            case "confirmed" -> RideRequestStatus.CONFIRMED;
            case "pending" -> RideRequestStatus.PENDING;
            case "declined" -> RideRequestStatus.DECLINED;
            case "canceled" -> RideRequestStatus.CANCELED;
            default -> throw new IllegalStatusException("Unexpected value: " + status.toLowerCase());
        };
    }


}
