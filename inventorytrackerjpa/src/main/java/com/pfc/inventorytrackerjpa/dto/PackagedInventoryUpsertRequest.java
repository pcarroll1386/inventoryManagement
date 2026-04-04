package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PackagedInventoryUpsertRequest {

    @NotNull
    @Positive
    private Long locationId;

    @NotNull
    @Positive
    private Long itemTypeId;

    private String nickname;

    @NotNull
    private Integer quantity;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
