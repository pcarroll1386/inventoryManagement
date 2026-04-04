package com.pfc.inventorytrackerjpa.services;

import com.pfc.inventorytrackerjpa.dto.BulkVolumeAdjustmentRequest;
import com.pfc.inventorytrackerjpa.dto.BulkInventoryUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.InventoryOverviewRow;
import com.pfc.inventorytrackerjpa.dto.PackagedInventoryUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.PackagedQuantityAdjustmentRequest;
import com.pfc.inventorytrackerjpa.dto.PolicyUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.SerializedItemTransferRequest;
import com.pfc.inventorytrackerjpa.dto.SerializedItemUpsertRequest;
import com.pfc.inventorytrackerjpa.entities.ItemKind;
import com.pfc.inventorytrackerjpa.entities.ItemType;
import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.entities.LocationBulkStock;
import com.pfc.inventorytrackerjpa.entities.LocationItemPolicy;
import com.pfc.inventorytrackerjpa.entities.LocationPackagedStock;
import com.pfc.inventorytrackerjpa.entities.SerializedItem;
import com.pfc.inventorytrackerjpa.entities.SerializedItemStatus;
import com.pfc.inventorytrackerjpa.entities.VolumeMeasurement;
import com.pfc.inventorytrackerjpa.repositories.InventoryOverviewProjection;
import com.pfc.inventorytrackerjpa.repositories.InventoryOverviewRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemTypeRepository;
import com.pfc.inventorytrackerjpa.repositories.LocationBulkStockRepository;
import com.pfc.inventorytrackerjpa.repositories.LocationItemPolicyRepository;
import com.pfc.inventorytrackerjpa.repositories.LocationPackagedStockRepository;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.repositories.SerializedItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class InventoryService {

    private static final String FIELD_LOCATION_ID = "locationId";
    private static final String FIELD_ITEM_TYPE_ID = "itemTypeId";
    private static final String FIELD_FROM_LOCATION_ID = "fromLocationId";
    private static final String FIELD_TO_LOCATION_ID = "toLocationId";
    private static final String FIELD_USER_ID = "userId";

    private final LocationItemPolicyRepository policyRepository;
    private final LocationPackagedStockRepository packagedStockRepository;
    private final LocationBulkStockRepository bulkStockRepository;
    private final SerializedItemRepository serializedItemRepository;
    private final InventoryOverviewRepository inventoryOverviewRepository;
    private final LocationRepository locationRepository;
    private final ItemTypeRepository itemTypeRepository;

    public InventoryService(
            LocationItemPolicyRepository policyRepository,
            LocationPackagedStockRepository packagedStockRepository,
            LocationBulkStockRepository bulkStockRepository,
            SerializedItemRepository serializedItemRepository,
            InventoryOverviewRepository inventoryOverviewRepository,
            LocationRepository locationRepository,
            ItemTypeRepository itemTypeRepository) {
        this.policyRepository = policyRepository;
        this.packagedStockRepository = packagedStockRepository;
        this.bulkStockRepository = bulkStockRepository;
        this.serializedItemRepository = serializedItemRepository;
        this.inventoryOverviewRepository = inventoryOverviewRepository;
        this.locationRepository = locationRepository;
        this.itemTypeRepository = itemTypeRepository;
    }

    @Transactional
    public void upsertPolicy(PolicyUpsertRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validatePolicyBounds(request.getMin(), request.getMax(), request.getReorderPoint(), request.getReorderQuantity());

        LocationItemPolicy policy = policyRepository
            .findByLocationIdAndItemTypeId(locationId, itemTypeId)
                .orElseGet(LocationItemPolicy::new);

        policy.setLocationId(locationId);
        policy.setItemTypeId(itemTypeId);
        policy.setMin(request.getMin());
        policy.setMax(request.getMax());
        policy.setReorderPoint(request.getReorderPoint());
        policy.setReorderQuantity(request.getReorderQuantity());

        policyRepository.save(policy);
    }

    @Transactional
    public void adjustPackagedQuantity(PackagedQuantityAdjustmentRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.PACKAGED_CONSUMABLE);

        LocationPackagedStock stock = packagedStockRepository
            .findByLocationIdAndItemTypeId(locationId, itemTypeId)
                .orElseGet(LocationPackagedStock::new);

        int currentQuantity = stock.getQuantity() == null ? 0 : stock.getQuantity();
        int nextQuantity = currentQuantity + request.getDeltaQuantity();
        if (nextQuantity < 0) {
            throw new InvalidDataException("Quantity adjustment would result in negative stock.");
        }

        stock.setLocationId(locationId);
        stock.setItemTypeId(itemTypeId);
        stock.setQuantity(nextQuantity);

        packagedStockRepository.save(stock);
    }

    @Transactional
    public void createPackagedInventory(PackagedInventoryUpsertRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.PACKAGED_CONSUMABLE);

        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new InvalidDataException("Packaged quantity cannot be null or negative.");
        }

        LocationPackagedStock stock = packagedStockRepository
                .findByLocationIdAndItemTypeId(locationId, itemTypeId)
                .orElseGet(LocationPackagedStock::new);

        stock.setLocationId(locationId);
        stock.setItemTypeId(itemTypeId);
        stock.setNickname(request.getNickname());
        stock.setQuantity(request.getQuantity());

        packagedStockRepository.save(stock);
    }

    @Transactional
    public void adjustBulkVolume(BulkVolumeAdjustmentRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.BULK_CONSUMABLE);

        VolumeMeasurement measurement = parseVolumeMeasurement(request.getVolumeMeasurement());
        LocationBulkStock stock = bulkStockRepository
            .findByLocationIdAndItemTypeId(locationId, itemTypeId)
                .orElseGet(LocationBulkStock::new);

        BigDecimal currentVolume = stock.getVolumeMl() == null ? BigDecimal.ZERO : stock.getVolumeMl();
        BigDecimal nextVolume = currentVolume.add(request.getDeltaVolumeMl());
        if (nextVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("Volume adjustment would result in negative stock.");
        }

        stock.setLocationId(locationId);
        stock.setItemTypeId(itemTypeId);
        stock.setVolumeMeasurement(measurement);
        stock.setVolumeMl(nextVolume);

        bulkStockRepository.save(stock);
    }

    @Transactional
    public void createBulkInventory(BulkInventoryUpsertRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.BULK_CONSUMABLE);

        if (request.getVolumeMl() == null || request.getVolumeMl().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("Bulk volume cannot be null or negative.");
        }

        VolumeMeasurement measurement = parseVolumeMeasurement(request.getVolumeMeasurement());
        LocationBulkStock stock = bulkStockRepository
                .findByLocationIdAndItemTypeId(locationId, itemTypeId)
                .orElseGet(LocationBulkStock::new);

        stock.setLocationId(locationId);
        stock.setItemTypeId(itemTypeId);
        stock.setNickname(request.getNickname());
        stock.setVolumeMeasurement(measurement);
        stock.setVolumeMl(request.getVolumeMl());

        bulkStockRepository.save(stock);
    }

    @Transactional
    public void createSerializedItem(SerializedItemUpsertRequest request) throws InvalidDataException {
        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.SERIALIZED_ASSET);

        if (serializedItemRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new InvalidDataException("Serialized item with this serial number already exists.");
        }

        SerializedItem serializedItem = new SerializedItem();
        applySerializedFields(serializedItem, request);
        serializedItem.setLocationId(locationId);
        serializedItem.setItemTypeId(itemTypeId);
        serializedItemRepository.save(serializedItem);
    }

    @Transactional
    public void updateSerializedItem(String serialNumber, SerializedItemUpsertRequest request) throws InvalidDataException {
        SerializedItem serializedItem = serializedItemRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidDataException("Serialized item not found for serial number."));

        if (!serialNumber.equals(request.getSerialNumber())) {
            throw new InvalidDataException("Path serial number must match request serial number.");
        }

        long locationId = requiredId(request.getLocationId(), FIELD_LOCATION_ID);
        long itemTypeId = requiredId(request.getItemTypeId(), FIELD_ITEM_TYPE_ID);

        validateLocationAndItemType(locationId, itemTypeId);
        validateItemKind(itemTypeId, ItemKind.SERIALIZED_ASSET);

        applySerializedFields(serializedItem, request);
        serializedItem.setLocationId(locationId);
        serializedItem.setItemTypeId(itemTypeId);
        serializedItemRepository.save(serializedItem);
    }

    @Transactional
    public void transferSerializedItem(String serialNumber, SerializedItemTransferRequest request) throws InvalidDataException {
        SerializedItem serializedItem = serializedItemRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidDataException("Serialized item not found for serial number."));

        long fromLocationId = requiredId(request.getFromLocationId(), FIELD_FROM_LOCATION_ID);
        long toLocationId = requiredId(request.getToLocationId(), FIELD_TO_LOCATION_ID);

        if (!locationRepository.existsById(fromLocationId)) {
            throw new InvalidDataException("From location does not exist.");
        }
        if (!locationRepository.existsById(toLocationId)) {
            throw new InvalidDataException("To location does not exist.");
        }

        if (serializedItem.getLocationId() == null || serializedItem.getLocationId() != fromLocationId) {
            throw new InvalidDataException("Serialized item is not currently at fromLocationId.");
        }

        serializedItem.setLocationId(toLocationId);
        serializedItem.setStatus(SerializedItemStatus.TRANSFERRED);
        String existingNotes = serializedItem.getNotes() == null ? "" : serializedItem.getNotes().trim();
        String transferNote = "TRANSFER: " + request.getReason();
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            transferNote = transferNote + " | " + request.getNotes().trim();
        }
        serializedItem.setNotes(existingNotes.isEmpty() ? transferNote : existingNotes + "\n" + transferNote);

        serializedItemRepository.save(serializedItem);
    }

    @Transactional(readOnly = true)
    public List<InventoryOverviewRow> getInventoryOverview(
            Long userId,
            List<Long> requestedLocationIds,
            List<Long> requestedItemTypeIds,
            String requestedItemKind) {
        return getInventoryOverviewByKind(userId, requestedLocationIds, requestedItemTypeIds, requestedItemKind);
    }

    @Transactional(readOnly = true)
    public List<InventoryOverviewRow> getPackagedInventory(
            Long userId,
            List<Long> requestedLocationIds,
            List<Long> requestedItemTypeIds) {
        return getInventoryOverviewByKind(userId, requestedLocationIds, requestedItemTypeIds, ItemKind.PACKAGED_CONSUMABLE.name());
    }

    @Transactional(readOnly = true)
    public List<InventoryOverviewRow> getBulkInventory(
            Long userId,
            List<Long> requestedLocationIds,
            List<Long> requestedItemTypeIds) {
        return getInventoryOverviewByKind(userId, requestedLocationIds, requestedItemTypeIds, ItemKind.BULK_CONSUMABLE.name());
    }

    @Transactional(readOnly = true)
    public List<InventoryOverviewRow> getSerializedInventory(
            Long userId,
            List<Long> requestedLocationIds,
            List<Long> requestedItemTypeIds) {
        return getInventoryOverviewByKind(userId, requestedLocationIds, requestedItemTypeIds, ItemKind.SERIALIZED_ASSET.name());
    }

    private List<InventoryOverviewRow> getInventoryOverviewByKind(
            Long userId,
            List<Long> requestedLocationIds,
            List<Long> requestedItemTypeIds,
            String requestedItemKind) {
        long resolvedUserId = Objects.requireNonNull(userId, FIELD_USER_ID + " is required.");
        List<Long> readableLocationIds = locationRepository.findReadableLocationIdsByUserId(resolvedUserId);
        if (readableLocationIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<InventoryOverviewProjection> rows = inventoryOverviewRepository.findAllByLocationIds(readableLocationIds);

        Set<Long> requestedLocationSet = requestedLocationIds == null ? null : new HashSet<>(requestedLocationIds);
        Set<Long> requestedItemTypeSet = requestedItemTypeIds == null ? null : new HashSet<>(requestedItemTypeIds);
        String normalizedItemKind = requestedItemKind == null ? null : requestedItemKind.trim().toUpperCase(Locale.ROOT);

        List<InventoryOverviewRow> mapped = new ArrayList<>();
        for (InventoryOverviewProjection row : rows) {
            if (matchesFilters(row, requestedLocationSet, requestedItemTypeSet, normalizedItemKind)) {
                mapped.add(toOverviewRow(row));
            }
        }

        return mapped;
    }

    private void validateLocationAndItemType(long locationId, long itemTypeId) throws InvalidDataException {
        Optional<Location> location = locationRepository.findById(locationId);
        if (location.isEmpty()) {
            throw new InvalidDataException("Location does not exist.");
        }
        if (itemTypeRepository.findById(itemTypeId).isEmpty()) {
            throw new InvalidDataException("Item type does not exist.");
        }
    }

    private long requiredId(Long value, String fieldName) throws InvalidDataException {
        if (value == null) {
            throw new InvalidDataException(fieldName + " is required.");
        }
        return value;
    }

    private boolean matchesFilters(
            InventoryOverviewProjection row,
            Set<Long> requestedLocationSet,
            Set<Long> requestedItemTypeSet,
            String normalizedItemKind) {

        boolean locationMatch = requestedLocationSet == null || requestedLocationSet.contains(row.getLocationId());
        boolean itemTypeMatch = requestedItemTypeSet == null || requestedItemTypeSet.contains(row.getItemTypeId());
        boolean itemKindMatch = normalizedItemKind == null || normalizedItemKind.isBlank() ||
                Objects.equals(
                        row.getItemKind() == null ? "" : row.getItemKind().toUpperCase(Locale.ROOT),
                        normalizedItemKind
                );

        return locationMatch && itemTypeMatch && itemKindMatch;
    }

    private void validateItemKind(long itemTypeId, ItemKind expectedKind) throws InvalidDataException {
        ItemType itemType = itemTypeRepository.findById(itemTypeId)
                .orElseThrow(() -> new InvalidDataException("Item type does not exist."));

        if (itemType.getItemKind() != expectedKind) {
            throw new InvalidDataException("Item type kind does not match operation.");
        }
    }

    private void validatePolicyBounds(Integer min, Integer max, Integer reorderPoint, Integer reorderQuantity)
            throws InvalidDataException {
        if (min != null && min < 0) {
            throw new InvalidDataException("Policy min cannot be negative.");
        }
        if (max != null && max < 0) {
            throw new InvalidDataException("Policy max cannot be negative.");
        }
        if (min != null && max != null && min > max) {
            throw new InvalidDataException("Policy min cannot exceed policy max.");
        }
        if (reorderPoint != null && reorderPoint < 0) {
            throw new InvalidDataException("Policy reorder point cannot be negative.");
        }
        if (reorderQuantity != null && reorderQuantity < 0) {
            throw new InvalidDataException("Policy reorder quantity cannot be negative.");
        }
    }

    private SerializedItemStatus parseSerializedStatus(String status) throws InvalidDataException {
        try {
            return SerializedItemStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new InvalidDataException("Invalid serialized item status: " + status);
        }
    }

    private VolumeMeasurement parseVolumeMeasurement(String value) throws InvalidDataException {
        try {
            return VolumeMeasurement.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new InvalidDataException("Invalid volume measurement: " + value);
        }
    }

    private void applySerializedFields(SerializedItem serializedItem, SerializedItemUpsertRequest request)
            throws InvalidDataException {
        serializedItem.setLocationId(request.getLocationId());
        serializedItem.setItemTypeId(request.getItemTypeId());
        serializedItem.setNickname(request.getNickname());
        serializedItem.setSerialNumber(request.getSerialNumber());
        serializedItem.setBrand(request.getBrand());
        serializedItem.setModel(request.getModel());
        serializedItem.setPurchasePrice(request.getPurchasePrice());
        serializedItem.setStatus(parseSerializedStatus(request.getStatus()));
        serializedItem.setPurchasedAt(request.getPurchasedAt());
        serializedItem.setNotes(request.getNotes());
    }

    private InventoryOverviewRow toOverviewRow(InventoryOverviewProjection projection) {
        InventoryOverviewRow row = new InventoryOverviewRow();
        row.setLocationId(projection.getLocationId());
        row.setItemTypeId(projection.getItemTypeId());
        row.setItemName(projection.getItemName());
        row.setItemKind(projection.getItemKind());
        row.setQuantity(projection.getQuantity());
        row.setVolumeMl(projection.getVolumeMl());
        row.setVolumeMeasurement(projection.getVolumeMeasurement());
        row.setSerialNumber(projection.getSerialNumber());
        row.setStatus(projection.getStatus());
        return row;
    }
}
