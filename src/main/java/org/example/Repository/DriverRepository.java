package org.example.Repository;

import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity,Long> {


    boolean existsByUser(UserEntity user);
    Optional<DriverEntity> findById(Long id);

}
