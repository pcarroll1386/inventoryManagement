# Inventory Model Implementation Plan (Greenfield)

## Objective
Build a clean inventory model for three item behaviors while keeping one shared item definition catalog.

Item behaviors:
- Serialized non-consumables (electronics, furniture, vehicles)
- Packaged consumables (boxes, cans, bottles)
- Bulk consumables (flour, sugar, dry beans)

Design rule:
- `item_type` remains the shared definition table.
- Location inventory is split by behavior-specific tables.

## Scope
- database/PaulCarroll_InventoryManagementDB.sql
- database/PaulCarroll_InventoryManagement_Data.sql

---

## Phase 1: Finalize Schema (Greenfield)

### 1.1 Item type classification
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Add enum `item_kind` with values `SERIALIZED_ASSET`, `PACKAGED_CONSUMABLE`, `BULK_CONSUMABLE`
- [x] Add `item_kind` column to `item_type` as NOT NULL
- [x] Add indexes for common lookup by (`item_kind`, `name`)

### 1.2 Policy table
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Keep `location_item_policy` with one row per (`location_id`, `item_type_id`)
- [x] Keep min/max and reorder fields in this table only

### 1.3 Split non-serialized inventory into two tables
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Replace `location_item_stock` with `location_packaged_stock`
- [x] `location_packaged_stock` columns: id, location_id, item_type_id, quantity
- [x] Add unique constraint on (`location_id`, `item_type_id`)
- [x] Add non-negative quantity check
- [x] Add foreign keys to location and item_type
- [x] Add table `location_bulk_stock`
- [x] `location_bulk_stock` columns: id, location_id, item_type_id, volume_ml, volume_measurement
- [x] Add unique constraint on (`location_id`, `item_type_id`)
- [x] Add non-negative volume check
- [x] Add foreign keys to location and item_type

### 1.4 Serialized inventory table
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Keep `serialized_item` table
- [x] Keep columns: serial_number, brand, model, purchase_price, status, purchased_at, notes
- [x] Add non-negative check for purchase_price
- [x] Keep unique constraint on serial_number
- [x] Keep indexes on (`location_id`, `item_type_id`)

### 1.5 Remove deprecated draft table definitions
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Remove old `item` table definition
- [x] Remove old `location_item_stock` definition
- [x] Remove related constraints/indexes for removed tables

---

## Phase 2: Seed and Bootstrap Data

### 2.1 Roles and app admin
Files:
- database/PaulCarroll_InventoryManagement_Data.sql

Tasks:
- [x] Insert or upsert app roles and location roles
- [x] Insert or upsert default app admin user
- [x] Ensure admin password hash is set

### 2.2 Seed item types and categories
Files:
- database/PaulCarroll_InventoryManagement_Data.sql

Tasks:
- [x] Seed item types with explicit `item_kind`
- [x] Seed categories
- [x] Seed item_type-to-category mappings

### 2.3 Seed location policies and stock
Files:
- database/PaulCarroll_InventoryManagement_Data.sql

Tasks:
- [x] Seed `location_item_policy` rows
- [x] Seed `location_packaged_stock` rows for packaged consumables
- [x] Seed `location_bulk_stock` rows for bulk consumables
- [x] Seed `serialized_item` rows for serialized assets

### 2.4 Seed verification queries
Files:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Add read-only checks for seeded row counts by table
- [x] Add read-only checks that each stock table references matching `item_kind`
- [x] Add read-only checks for serial uniqueness and non-negative quantities/volumes

---

## Phase 3: Unified Read Model for Front End

### 3.1 Inventory overview view
File:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Create `inventory_overview` view using `UNION ALL` over:
  - `location_packaged_stock`
  - `location_bulk_stock`
  - `serialized_item`
- [x] Normalize columns for front-end list rendering:
  - location_id
  - item_type_id
  - item_name
  - item_kind
  - quantity
  - volume_ml
  - volume_measurement
  - serial_number
  - status

### 3.2 Access-scoped read pattern
Files:
- database/PaulCarroll_InventoryManagementDB.sql

Tasks:
- [x] Document query pattern that filters inventory by readable location ids
- [x] Add sample SQL for querying all readable location inventory in one list

---

## Phase 4: API Refactor Preparation Checklist

Tasks:
- [x] Define endpoint contract for policy upsert
- [x] Define endpoint contract for packaged quantity adjustments
- [x] Define endpoint contract for bulk volume adjustments
- [x] Define endpoint contract for serialized item create/update/transfer
- [x] Define endpoint contract for unified inventory list reads

---

## Suggested Build Order

1. Finalize schema (item_kind + split stock tables)
2. Finalize seed script for roles/admin + item types/categories
3. Seed stock and serialized sample rows
4. Add verification queries
5. Add inventory_overview view
6. Validate front-end single-list query shape
7. Start Java refactor after schema and seed are stable

---

## Definition of Done
- [ ] `item_type` classifies all items using `item_kind`
- [ ] Packaged and bulk stock are stored in separate tables
- [ ] Serialized assets are stored one row per unit with unique serial_number
- [ ] Min/max policy is per (`location_id`, `item_type_id`)
- [ ] One SQL view supports a unified front-end inventory list
- [x] One SQL view supports a unified front-end inventory list
- [ ] Seed script creates working baseline roles, app admin, item catalog, and stock data
