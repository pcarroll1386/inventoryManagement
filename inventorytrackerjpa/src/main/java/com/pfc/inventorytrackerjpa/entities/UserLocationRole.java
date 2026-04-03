package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "user_location_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "location_id", "role_id"})
)
public class UserLocationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLocationRole)) return false;
        UserLocationRole that = (UserLocationRole) o;
        return id == that.id
                && Objects.equals(user, that.user)
                && Objects.equals(location, that.location)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, location, role);
    }
}