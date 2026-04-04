# DTO and Service Layer Rollout Plan

## Goal
Migrate the app to a cleaner architecture where:
- Controllers handle HTTP concerns only.
- Services own business logic and transactions.
- Repositories remain data-access only.
- API request bodies use DTOs instead of JPA entities.

## Guiding Decisions
1. Use request DTOs for create/edit operations across all controllers.
2. Keep entity responses initially (phase 1), then optionally move to response DTOs (phase 2).
3. Add one service per domain area: User, Role, UserLocationRole, Item, ItemType, Category, Location.
4. Place `@Transactional` on service write methods.

## Phase 1: Establish the Pattern (UserLocationRole)
1. Create `UserLocationRoleService`.
2. Move all business rules from `UserLocationRoleController` into service methods:
   - resolve user/location/role by ID
   - enforce role scope (`LOCATION`)
   - prevent duplicate assignment
   - ensure user-location link consistency
   - delete cleanup behavior
3. Keep `UserLocationRoleController` thin: accept DTO, call service, return response.
4. Add service-focused tests for happy and error paths.

## Phase 2: Request DTO Rollout Across App
Convert request bodies to DTOs in:
1. `UserController`
2. `RoleController`
3. `ItemController`
4. `ItemTypeController`
5. `CategoryController`
6. `LocationController`

DTO rules:
- Include only writable fields.
- Use IDs for relationships (avoid nested entity payloads).
- Add bean validation annotations (`@NotNull`, `@Positive`, `@NotBlank`, etc.).

## Phase 3: Service Layer Rollout by Domain
Implement/refactor services in this order:
1. `UserLocationRoleService`
2. `RoleService`
3. `UserService`
4. `ItemService`
5. `ItemTypeService`
6. `CategoryService`
7. `LocationService`

Service responsibilities:
- ID resolution and guard checks
- business rule enforcement
- transaction boundaries
- cross-entity coordination

## Phase 4: Controller Thinning
For each controller:
1. Accept DTO request.
2. Call one service method.
3. Return response.
4. Remove resolver/helper business logic from controller.

## Phase 5: Error Handling Consistency
1. Keep centralized handling in `ExceptionHandlers`.
2. Align/expand exception types for service-level validation failures.
3. Standardize error payload shape and status mapping.

## Phase 6: Tests and Validation
1. Add/expand service tests for business rules.
2. Add controller tests for validation and status codes.
3. Keep compile and tests green per domain slice (not only at the end).

## Optional Phase 7: Response DTOs
After request DTO and service rollout:
1. Add response DTOs to avoid exposing entity graphs.
2. Map entity -> response DTO in service/controller layer.
3. Reduce recursion/lazy-loading exposure in API responses.

## Phase 8: Inventory Domain Refactor (Phase 4 Contracts -> Implementation)

### 8.1 Entity Strategy (Replace legacy item-centric model)
Current issue:
1. Legacy `Item` entity maps to `item` table, but schema now uses:
   - `location_item_policy`
   - `location_packaged_stock`
   - `location_bulk_stock`
   - `serialized_item`
   - `inventory_overview` view

Plan:
1. Remove legacy `Item`/`item` table code path completely.
2. Introduce only schema-aligned entities (or projections):
   - `LocationItemPolicy`
   - `LocationPackagedStock`
   - `LocationBulkStock`
   - `SerializedItem`
3. For unified list reads, prefer projection over full entity graph:
   - map `inventory_overview` rows directly to response DTO (`InventoryOverviewRow`) using repository projection query.
4. Delete old `/items` flow instead of preserving compatibility wrappers.

### 8.2 Repository Layer Design
Create dedicated repositories:
1. `LocationItemPolicyRepository`
   - finder by `(locationId, itemTypeId)`
2. `LocationPackagedStockRepository`
   - finder by `(locationId, itemTypeId)`
   - list by `locationId`
3. `LocationBulkStockRepository`
   - finder by `(locationId, itemTypeId)`
   - list by `locationId`
4. `SerializedItemRepository`
   - finder by `serialNumber`
   - list by `locationId` and optional `itemTypeId`
5. `InventoryOverviewRepository` (native query/projection)
   - method for readable locations + optional filters (`locationIds`, `itemTypeIds`, `itemKind`)

Query principles:
1. Command operations use table repositories (no direct writes through view).
2. Read operations for front-end list use `inventory_overview` query.
3. Keep filter behavior optional and composable.

### 8.3 Service Layer Responsibilities
Create `InventoryService` and move controller logic there.

Methods:
1. `upsertPolicy(PolicyUpsertRequest)`
   - validate location/item type
   - create or update policy row
2. `adjustPackagedQuantity(PackagedQuantityAdjustmentRequest)`
   - validate `item_kind = PACKAGED_CONSUMABLE`
   - apply delta with non-negative guard
3. `adjustBulkVolume(BulkVolumeAdjustmentRequest)`
   - validate `item_kind = BULK_CONSUMABLE`
   - apply delta with non-negative guard
4. `createSerializedItem(SerializedItemUpsertRequest)`
   - validate `item_kind = SERIALIZED_ASSET`
   - enforce unique serial
5. `updateSerializedItem(serialNumber, SerializedItemUpsertRequest)`
   - load by serial and update mutable fields
6. `transferSerializedItem(serialNumber, SerializedItemTransferRequest)`
   - validate from/to locations
   - require current row location matches `fromLocationId`
   - move to `toLocationId`, set status/audit notes
7. `getInventoryOverview(userId, filters...)`
   - compute readable location IDs
   - query view and map to DTO list

Transactional rules:
1. Write methods: `@Transactional`
2. Read method: `@Transactional(readOnly = true)`

### 8.4 Controller Integration
`InventoryContractController` should become thin:
1. Accept DTO
2. Call `InventoryService`
3. Return status/body

Endpoint return guidance:
1. Policy and adjustments: `200` with updated summary DTO (optional) or `204`
2. Serialized create: `201`
3. Serialized update/transfer: `200`
4. Overview: `200` with `List<InventoryOverviewRow>`

### 8.5 Inventory Getter Implementation for Front End
Use one read model getter based on the view:
1. Service method receives optional filters.
2. Repository query applies filters only when values are present.
3. Output shape matches `InventoryOverviewRow` exactly:
   - `locationId`
   - `itemTypeId`
   - `itemName`
   - `itemKind`
   - `quantity`
   - `volumeMl`
   - `volumeMeasurement`
   - `serialNumber`
   - `status`

Reasoning:
1. Avoids loading multiple tables and stitching results in Java.
2. Keeps front-end response stable and simple.
3. Preserves database as source of truth for unified inventory semantics.

### 8.6 Implementation Order (Inventory Slice)
1. Remove legacy `Item` entity/repository/controller/service usage.
2. Add new entities + repositories.
3. Implement `InventoryService` write methods.
4. Implement overview read query + mapping.
5. Wire controller to service.
6. Add service tests for each command/read path.
7. Add controller tests for validation and status codes.

### 8.7 Done Criteria for Inventory Slice
1. No reads or writes to legacy `item` table path.
2. Contract endpoints perform real DB operations.
3. Inventory overview endpoint returns real filtered data.
4. All inventory rules validated by service tests.
5. Legacy item flow removed.

## Execution Strategy
1. Work in small vertical slices (one domain at a time).
2. Complete each slice end-to-end (DTO + service + tests) before moving on.
3. Avoid big-bang refactors across all files in one pass.

## Definition of Done
1. No controller accepts JPA entities in request bodies.
2. Business logic resides in services, not controllers.
3. Service write operations are transactional.
4. Endpoint behavior remains stable unless intentionally changed.
5. Build passes and tests pass.
