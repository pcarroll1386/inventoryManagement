# Phase 2 Todo Checklist

Use this checklist to track Phase 2 from `DTO_AND_SERVICE_LAYER_PLAN.md`.

## Scope
- [ ] Confirm all request bodies use DTOs (no JPA entities in request payloads)
- [ ] Confirm relationship fields in DTOs use IDs only (no nested entity payloads)
- [ ] Confirm DTOs include appropriate bean validation annotations

## 1. UserController
- [x] Create endpoint accepts `UserCreateRequest`
- [x] Update endpoint accepts `UserUpdateRequest`
- [x] Controller delegates business logic to `UserService`
- [x] DTO validation for User requests is in place
- [ ] Add/verify tests for User request validation and status codes

## 2. RoleController
- [x] Read endpoints use RoleService with proper @Transactional(readOnly = true)
- [x] Controller delegates to service (thin HTTP layer)
- [x] No create/update/delete endpoints (fixed role catalog by design)
- [x] Unused imports cleaned up
- [ ] Add/verify tests for Role read operations and error handling

## 3. ItemController
- [ ] Create endpoint accepts Item request DTO
- [ ] Update endpoint accepts Item request DTO
- [ ] Relationship fields use IDs only
- [ ] Bean validation annotations are applied
- [ ] Controller is thin (HTTP concerns only)
- [ ] Add/verify tests for Item request validation and status codes

## 4. ItemTypeController
- [ ] Create endpoint accepts ItemType request DTO
- [ ] Update endpoint accepts ItemType request DTO
- [ ] Relationship fields use IDs only
- [ ] Bean validation annotations are applied
- [ ] Controller is thin (HTTP concerns only)
- [ ] Add/verify tests for ItemType request validation and status codes

## 5. CategoryController
- [ ] Create endpoint accepts Category request DTO
- [ ] Update endpoint accepts Category request DTO
- [ ] Relationship fields use IDs only
- [ ] Bean validation annotations are applied
- [ ] Controller is thin (HTTP concerns only)
- [ ] Add/verify tests for Category request validation and status codes

## 6. LocationController
- [ ] Create endpoint accepts Location request DTO
- [ ] Update endpoint accepts Location request DTO
- [ ] Relationship fields use IDs only
- [ ] Bean validation annotations are applied
- [ ] Controller is thin (HTTP concerns only)
- [ ] Add/verify tests for Location request validation and status codes

## Completion Gates
- [ ] `./mvnw -q -DskipTests compile` passes
- [ ] Relevant tests pass for each completed controller slice
- [ ] No controller in scope accepts JPA entities in request bodies
- [ ] Phase 2 checklist fully complete
