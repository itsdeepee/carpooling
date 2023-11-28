package org.example.Repository;

import jakarta.validation.constraints.NotNull;
import org.example.Model.Entities.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<RideEntity,Long> {

    @Override
    @NotNull
    List<RideEntity> findAll();
    List<RideEntity> findByDateTimeOfRideAfter(LocalDateTime localDateTime);
}
