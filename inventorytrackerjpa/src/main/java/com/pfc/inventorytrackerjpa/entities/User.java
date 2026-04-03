package com.pfc.inventorytrackerjpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "employee_identification", nullable = false)
    private String employeeIdentification;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_location",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();

        @JsonIgnore
        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        private Set<UserLocationRole> locationRoles = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        if (role == null) {
            return;
        }
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<UserLocationRole> getLocationRoles() {
        return locationRoles;
    }

    public void setLocationRoles(Set<UserLocationRole> locationRoles) {
        this.locationRoles = locationRoles;
    }

    public void addLocation(Location location) {
        if (location == null) {
            return;
        }
        this.locations.add(location);
        location.getUsers().add(this);
    }

    public void removeLocation(Location location) {
        if (location == null) {
            return;
        }
        this.locations.remove(location);
        location.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id
                && enabled == user.enabled
                && Objects.equals(username, user.username)
                && Objects.equals(name, user.name)
                && Objects.equals(employeeIdentification, user.employeeIdentification)
                && Objects.equals(supervisorId, user.supervisorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, employeeIdentification, supervisorId, enabled);
    }
}

