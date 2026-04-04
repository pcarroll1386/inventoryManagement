package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.SerializedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerializedItemRepository extends JpaRepository<SerializedItem, Long> {
    Optional<SerializedItem> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
    List<SerializedItem> findAllByLocationId(Long locationId);
    List<SerializedItem> findAllByLocationIdAndItemTypeId(Long locationId, Long itemTypeId);
}
