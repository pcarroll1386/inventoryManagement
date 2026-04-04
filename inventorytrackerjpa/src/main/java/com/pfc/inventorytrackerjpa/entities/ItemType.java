package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item_type")
public class ItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String description;

        @Enumerated(EnumType.STRING)
        @Column(name = "item_kind", nullable = false)
        private ItemKind itemKind;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_type_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ItemKind getItemKind() {
        return itemKind;
    }

    public void setItemKind(ItemKind itemKind) {
        this.itemKind = itemKind;
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
        if (!(o instanceof ItemType)) return false;
        ItemType itemType = (ItemType) o;
        return Objects.equals(id, itemType.id)
                && Objects.equals(name, itemType.name)
                && Objects.equals(description, itemType.description)
                && itemKind == itemType.itemKind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, itemKind);
    }
}
