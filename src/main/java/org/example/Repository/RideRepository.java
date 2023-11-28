package org.example.Repository;

import org.example.Model.Entities.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<RideEntity,Long> {

    @Override
    List<RideEntity> findAll();
}
