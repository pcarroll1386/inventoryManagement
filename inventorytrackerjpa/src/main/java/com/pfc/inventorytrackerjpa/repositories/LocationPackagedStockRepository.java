package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.LocationPackagedStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationPackagedStockRepository extends JpaRepository<LocationPackagedStock, Long> {
    Optional<LocationPackagedStock> findByLocationIdAndItemTypeId(Long locationId, Long itemTypeId);
    List<LocationPackagedStock> findAllByLocationId(Long locationId);
}
