package com.pfc.inventorytrackerjpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryOverviewRepository extends Repository<InventoryOverviewProjection, Long> {

    @Query(value = "SELECT " +
            "location_id AS locationId, " +
            "item_type_id AS itemTypeId, " +
            "item_name AS itemName, " +
            "CAST(item_kind AS VARCHAR) AS itemKind, " +
            "quantity AS quantity, " +
            "volume_ml AS volumeMl, " +
            "CAST(volume_measurement AS VARCHAR) AS volumeMeasurement, " +
            "serial_number AS serialNumber, " +
            "CAST(status AS VARCHAR) AS status " +
            "FROM inventory_overview " +
            "WHERE location_id IN (:locationIds) " +
            "ORDER BY location_id, item_name, serial_number", nativeQuery = true)
    List<InventoryOverviewProjection> findAllByLocationIds(@Param("locationIds") List<Long> locationIds);
}
