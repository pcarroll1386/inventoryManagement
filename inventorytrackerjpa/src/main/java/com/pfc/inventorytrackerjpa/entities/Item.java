package com.pfc.inventorytrackerjpa.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "locationId", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "itemTypeId", nullable = false)
    private ItemType itemType;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column
    private BigDecimal price;

    @Column
    private int max;

    @Column
    private int min;

    @Column
    private int quantity;

    @Column(name = "volume_ml")
    private BigDecimal volumeMl;

    @Enumerated(EnumType.STRING)
    @Column(name = "volume_measurement")
    private VolumeMeasurement volumeMeasurement;

    /**
     * True  = bulk storage item (e.g. macaroni noodles) — consumed ml by ml and refilled.
     * False = pre-packaged item (e.g. box of mac & cheese) — consumed whole and removed.
     */
    @Column(name = "is_bulk", nullable = false)
    private boolean isBulk = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public boolean isBulk() {
        return isBulk;
    }

    public void setBulk(boolean isBulk) {
        this.isBulk = isBulk;
    }

    // -------------------------------------------------------------------------
    // Volume conversion helpers
    // -------------------------------------------------------------------------

    /**
     * Converts the stored {@code volumeMl} into the unit defined by
     * {@code volumeMeasurement} and returns the result.
     * Returns {@code null} if either field is not set.
     */
    public BigDecimal getConvertedVolume() {
        if (volumeMl == null || volumeMeasurement == null) return null;
        return volumeMl.divide(
                BigDecimal.valueOf(volumeMeasurement.getMlEquivalent()), 6, RoundingMode.HALF_UP);
    }

    /**
     * Converts the stored {@code volumeMl} into any target unit on demand.
     * Useful when you want a one-off conversion without changing the stored
     * {@code volumeMeasurement}.
     *
     * @param targetUnit the unit to convert into
     * @return the converted value, or {@code null} if {@code volumeMl} is not set
     */
    public BigDecimal getConvertedVolume(VolumeMeasurement targetUnit) {
        if (volumeMl == null || targetUnit == null) return null;
        return volumeMl.divide(
                BigDecimal.valueOf(targetUnit.getMlEquivalent()), 6, RoundingMode.HALF_UP);
    }

    /**
     * Accepts a value in any supported unit, converts it to milliliters, and
     * stores the result in {@code volumeMl}. Use this when the caller knows the
     * volume in a non-ml unit (e.g. 2 cups) and wants to persist the ml base.
     *
     * @param value the volume expressed in {@code unit}
     * @param unit  the unit {@code value} is expressed in
     */
    public void setVolumeFromMeasurement(BigDecimal value, VolumeMeasurement unit) {
        if (value == null || unit == null) {
            this.volumeMl = null;
            return;
        }
        this.volumeMl = value.multiply(BigDecimal.valueOf(unit.getMlEquivalent()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id == item.id
                && max == item.max
                && min == item.min
                && quantity == item.quantity
                && Objects.equals(location, item.location)
                && Objects.equals(itemType, item.itemType)
                && Objects.equals(serialNumber, item.serialNumber)
                && Objects.equals(price, item.price)
                && Objects.equals(volumeMl, item.volumeMl)
                && volumeMeasurement == item.volumeMeasurement
                && isBulk == item.isBulk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, itemType, serialNumber, price, max, min, quantity, volumeMl, volumeMeasurement, isBulk);
    }
}
