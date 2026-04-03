package com.pfc.inventorytrackerjpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UserCreateRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String employeeIdentification;

    private Boolean enabled;

    @NotNull
    @Positive
    private Long appRoleId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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