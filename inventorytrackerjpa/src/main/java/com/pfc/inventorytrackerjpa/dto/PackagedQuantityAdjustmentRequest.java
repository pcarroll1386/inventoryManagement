package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PackagedQuantityAdjustmentRequest {

    @NotNull
    @Positive
    private Long locationId;

    @NotNull
    @Positive
    private Long itemTypeId;

    @NotNull
    private Integer deltaQuantity;

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

    public Integer getDeltaQuantity() {
        return deltaQuantity;
    }

    public void setDeltaQuantity(Integer deltaQuantity) {
        this.deltaQuantity = deltaQuantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
