package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.LocationItemPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationItemPolicyRepository extends JpaRepository<LocationItemPolicy, Long> {
    Optional<LocationItemPolicy> findByLocationIdAndItemTypeId(Long locationId, Long itemTypeId);
}
