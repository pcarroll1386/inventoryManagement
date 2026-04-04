package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.LocationBulkStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationBulkStockRepository extends JpaRepository<LocationBulkStock, Long> {
    Optional<LocationBulkStock> findByLocationIdAndItemTypeId(Long locationId, Long itemTypeId);
    List<LocationBulkStock> findAllByLocationId(Long locationId);
}
