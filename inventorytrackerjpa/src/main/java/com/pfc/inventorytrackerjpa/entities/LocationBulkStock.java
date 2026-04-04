package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(
        name = "location_bulk_stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"location_id", "item_type_id"})
)
public class LocationBulkStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "item_type_id", nullable = false)
    private Long itemTypeId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "volume_ml", nullable = false)
    private BigDecimal volumeMl;

    @Enumerated(EnumType.STRING)
    @Column(name = "volume_measurement", nullable = false)
    private VolumeMeasurement volumeMeasurement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(BigDecimal volumeMl) {
        this.volumeMl = volumeMl;
    }

    public VolumeMeasurement getVolumeMeasurement() {
        return volumeMeasurement;
    }

    public void setVolumeMeasurement(VolumeMeasurement volumeMeasurement) {
        this.volumeMeasurement = volumeMeasurement;
    }
}
