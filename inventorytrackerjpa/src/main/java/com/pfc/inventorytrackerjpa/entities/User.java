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

    @Column(nullable = false)
    private boolean enabled = true;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "app_role_id", nullable = false)
        private Role appRole;

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

    protected User() {
        // Required by JPA.
    }

    public User(String username, String password, String name, String employeeIdentification, boolean enabled) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.employeeIdentification = employeeIdentification;
        this.enabled = enabled;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getAppRole() {
        return appRole;
    }

    public void setAppRole(Role appRole) {
        this.appRole = appRole;
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
                && Objects.equals(employeeIdentification, user.employeeIdentification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, employeeIdentification, enabled);
    }
}

