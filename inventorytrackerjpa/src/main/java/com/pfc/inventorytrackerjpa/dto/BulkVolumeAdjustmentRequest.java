package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class BulkVolumeAdjustmentRequest {

    @NotNull
    @Positive
    private Long locationId;

    @NotNull
    @Positive
    private Long itemTypeId;

    @NotNull
    private BigDecimal deltaVolumeMl;

    @NotBlank
    private String volumeMeasurement;

    @NotBlank
    private String reason;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public BigDecimal getDeltaVolumeMl() {
        return deltaVolumeMl;
    }

    public void setDeltaVolumeMl(BigDecimal deltaVolumeMl) {
        this.deltaVolumeMl = deltaVolumeMl;
    }

    public String getVolumeMeasurement() {
        return volumeMeasurement;
    }

    public void setVolumeMeasurement(String volumeMeasurement) {
        this.volumeMeasurement = volumeMeasurement;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
