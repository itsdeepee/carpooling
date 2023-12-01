package org.example.Repository;


import org.example.Model.Entities.RideRequestEntity;
import org.example.Model.Entities.UserEntity;
import org.example.Model.RideRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequestEntity,Long> {

    Optional<RideRequestEntity> findByRequestID(Long rideRequestID);
    List<RideRequestEntity> findByPassenger(UserEntity passenger);
    List<RideRequestEntity> findByPassengerAndStatus(UserEntity userEntity, RideRequestStatus status);

   void deleteByRequestID(Long rideRequestId);

}
