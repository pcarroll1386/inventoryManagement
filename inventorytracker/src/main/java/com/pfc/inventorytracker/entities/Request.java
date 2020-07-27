/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author pfcar
 */
public class Request {
    private int id;
    private LocalDateTime submitDate;
    private LocalDateTime filledDate;
    private String notes;
    private int status;
    private int type;
    private int priority;
    private Location location;
    private List<Item> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public LocalDateTime getFilledDate() {
        return filledDate;
    }

    public void setFilledDate(LocalDateTime filledDate) {
        this.filledDate = filledDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.submitDate);
        hash = 97 * hash + Objects.hashCode(this.filledDate);
        hash = 97 * hash + Objects.hashCode(this.notes);
        hash = 97 * hash + this.status;
        hash = 97 * hash + this.type;
        hash = 97 * hash + this.priority;
        hash = 97 * hash + Objects.hashCode(this.location);
        hash = 97 * hash + Objects.hashCode(this.items);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Request other = (Request) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        if (!Objects.equals(this.notes, other.notes)) {
            return false;
        }
        if (!Objects.equals(this.submitDate, other.submitDate)) {
            return false;
        }
        if (!Objects.equals(this.filledDate, other.filledDate)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        return true;
    }
    
}
