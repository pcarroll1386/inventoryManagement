package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Item {

    @GeneratedValue
    @Id
    private UUID id;

    @Column
    @NotNull
    private String name;

    @Column(name = "model_number")
    private String modelNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

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

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getId().equals(item.getId()) && getName().equals(item.getName()) && Objects.equals(getModelNumber(), item.getModelNumber()) && Objects.equals(getCategories(), item.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getModelNumber(), getCategories());
    }
}
