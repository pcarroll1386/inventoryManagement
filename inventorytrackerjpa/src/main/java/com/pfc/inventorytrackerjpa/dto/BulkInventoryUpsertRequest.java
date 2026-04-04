package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class BulkInventoryUpsertRequest {

    @NotNull
    @Positive
    private Long locationId;

    @NotNull
    @Positive
    private Long itemTypeId;

    private String nickname;

    @NotNull
    private BigDecimal volumeMl;

    @NotBlank
    private String volumeMeasurement;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(BigDecimal volumeMl) {
        this.volumeMl = volumeMl;
    }

    public String getVolumeMeasurement() {
        return volumeMeasurement;
    }

    public void setVolumeMeasurement(String volumeMeasurement) {
        this.volumeMeasurement = volumeMeasurement;
    }
}
