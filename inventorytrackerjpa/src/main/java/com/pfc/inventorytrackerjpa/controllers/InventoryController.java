package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.dto.BulkVolumeAdjustmentRequest;
import com.pfc.inventorytrackerjpa.dto.BulkInventoryUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.InventoryOverviewRow;
import com.pfc.inventorytrackerjpa.dto.PackagedInventoryUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.PackagedQuantityAdjustmentRequest;
import com.pfc.inventorytrackerjpa.dto.PolicyUpsertRequest;
import com.pfc.inventorytrackerjpa.dto.SerializedItemTransferRequest;
import com.pfc.inventorytrackerjpa.dto.SerializedItemUpsertRequest;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping(value = "/policies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> upsertPolicy(@Valid @RequestBody PolicyUpsertRequest request) throws InvalidDataException {
        inventoryService.upsertPolicy(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/packaged/adjustments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adjustPackagedQuantity(@Valid @RequestBody PackagedQuantityAdjustmentRequest request)
            throws InvalidDataException {
        inventoryService.adjustPackagedQuantity(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/packaged", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPackagedInventory(@Valid @RequestBody PackagedInventoryUpsertRequest request)
            throws InvalidDataException {
        inventoryService.createPackagedInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/bulk/adjustments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adjustBulkVolume(@Valid @RequestBody BulkVolumeAdjustmentRequest request)
            throws InvalidDataException {
        inventoryService.adjustBulkVolume(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBulkInventory(@Valid @RequestBody BulkInventoryUpsertRequest request)
            throws InvalidDataException {
        inventoryService.createBulkInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/serialized", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createSerializedItem(@Valid @RequestBody SerializedItemUpsertRequest request)
            throws InvalidDataException {
        inventoryService.createSerializedItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/serialized/{serialNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateSerializedItem(
            @PathVariable("serialNumber") String serialNumber,
            @Valid @RequestBody SerializedItemUpsertRequest request) throws InvalidDataException {
        inventoryService.updateSerializedItem(serialNumber, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/serialized/{serialNumber}/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> transferSerializedItem(
            @PathVariable("serialNumber") String serialNumber,
            @Valid @RequestBody SerializedItemTransferRequest request) throws InvalidDataException {
        inventoryService.transferSerializedItem(serialNumber, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventoryOverviewRow>> getInventoryOverview(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "locationId", required = false) List<Long> locationIds,
            @RequestParam(value = "itemTypeId", required = false) List<Long> itemTypeIds,
            @RequestParam(value = "itemKind", required = false) String itemKind) {
        return ResponseEntity.ok(inventoryService.getInventoryOverview(userId, locationIds, itemTypeIds, itemKind));
    }

    @GetMapping(value = "/packaged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventoryOverviewRow>> getAllPackagedInventory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "locationId", required = false) List<Long> locationIds,
            @RequestParam(value = "itemTypeId", required = false) List<Long> itemTypeIds) {
        return ResponseEntity.ok(inventoryService.getPackagedInventory(userId, locationIds, itemTypeIds));
    }

    @GetMapping(value = "/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventoryOverviewRow>> getAllBulkInventory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "locationId", required = false) List<Long> locationIds,
            @RequestParam(value = "itemTypeId", required = false) List<Long> itemTypeIds) {
        return ResponseEntity.ok(inventoryService.getBulkInventory(userId, locationIds, itemTypeIds));
    }

    @GetMapping(value = "/serialized", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventoryOverviewRow>> getAllSerializedInventory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "locationId", required = false) List<Long> locationIds,
            @RequestParam(value = "itemTypeId", required = false) List<Long> itemTypeIds) {
        return ResponseEntity.ok(inventoryService.getSerializedInventory(userId, locationIds, itemTypeIds));
    }
}
