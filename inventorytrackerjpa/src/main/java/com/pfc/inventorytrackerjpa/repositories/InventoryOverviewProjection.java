package com.pfc.inventorytrackerjpa.repositories;

import java.math.BigDecimal;

public interface InventoryOverviewProjection {
    Long getLocationId();
    Long getItemTypeId();
    String getItemName();
    String getItemKind();
    Integer getQuantity();
    BigDecimal getVolumeMl();
    String getVolumeMeasurement();
    String getSerialNumber();
    String getStatus();
}
