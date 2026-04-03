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
