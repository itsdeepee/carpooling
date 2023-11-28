package org.example.Repository;

import org.example.Model.Entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity,Long> {

    boolean existsByFullPlaceName(String fullPlaceName);
    Optional<LocationEntity> findByFullPlaceName(String fullPlaceName);
}
