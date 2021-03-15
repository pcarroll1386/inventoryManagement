package com.pfc.inventorytracker.entities;

import java.math.BigDecimal;
import java.util.Set;
import java.util.Objects;

public class LocationItem extends Item {

    private int locationItemId;
    private int inInventory;
    private int min;
    private int max;
    private int quantity;
    private Set<String> serialNumbers;

    public int getLocationItemId() {
        return locationItemId;
    }

    public void setLocationItemId(int locationItemId) {
        this.locationItemId = locationItemId;
    }

    public int getInInventory() {
        return inInventory;
    }

    public void setInInventory(int inInventory) {
        this.inInventory = inInventory;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<String> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSeirialNumbers(Set<String> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }
}
