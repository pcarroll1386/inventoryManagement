package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Category {

    @GeneratedValue
    @Id
    private UUID id;

    @Column
    @NotNull
    private String name;

//    @ManyToMany(mappedBy = "categories")
//    private Set<Item> items;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId().equals(category.getId()) && getName().equals(category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
