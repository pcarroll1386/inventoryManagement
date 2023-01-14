package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Location {

    @GeneratedValue
    @Id
    private UUID id;

    @Column
    @NotNull
    private String name;

    @Column
    private String description;

    @Column
    private boolean template;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return isTemplate() == location.isTemplate() && getId().equals(location.getId()) && getName().equals(location.getName()) && Objects.equals(getDescription(), location.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), isTemplate());
    }
}
