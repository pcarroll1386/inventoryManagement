package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UserLocationRoleRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long locationId;

    @NotNull
    @Positive
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}