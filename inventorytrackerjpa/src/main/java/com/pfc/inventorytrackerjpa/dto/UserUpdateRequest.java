package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;
public class UserUpdateRequest {

    @Size(min = 8)
    private String password;

    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String employeeIdentification;

    private Boolean enabled;

    @Positive
    private Long appRoleId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeIdentification() {
        return employeeIdentification;
    }

    public void setEmployeeIdentification(String employeeIdentification) {
        this.employeeIdentification = employeeIdentification;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(Long appRoleId) {
        this.appRoleId = appRoleId;
    }
}