-- Run this section separately in DBeaver (connected to postgres DB), then reconnect to inventorymanagementdbtest.
DROP DATABASE IF EXISTS inventorymanagementdbtest;
CREATE DATABASE inventorymanagementdbtest;

-- After reconnecting to inventorymanagementdbtest, run everything below.

CREATE TABLE "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    app_role_id INT NOT NULL,
    employee_identification varchar(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL
);

CREATE TYPE role_scope AS ENUM (
    'APP',
    'LOCATION'
);

CREATE TABLE "role"(
    id SERIAL PRIMARY KEY,
    "role" VARCHAR(255) UNIQUE NOT NULL,
    scope role_scope NOT NULL
);

CREATE TABLE location(
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255),
    template BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE user_location(
    location_id INT NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY(location_id, user_id)
);

CREATE TABLE user_location_role(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    location_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT uq_user_location_role UNIQUE (user_id, location_id, role_id)
);

CREATE TABLE job(
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    template BOOLEAN NOT NULL DEFAULT FALSE,
    location_id INT NOT NULL
);

CREATE TABLE request(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    user_id int NOT NULL,
    submit_date TIMESTAMP,
    fill_date TIMESTAMP,
    notes TEXT,
    "status" INT NOT NULL DEFAULT 0,
    "type" INT NOT NULL DEFAULT 0,
    priority INT NOT NULL DEFAULT 0,
    work_order VARCHAR(255)
);

CREATE TABLE item_type(
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL
);

CREATE TABLE job_item(
    job_id INT NOT NULL,
    item_type_id INT NOT NULL,
    PRIMARY KEY (job_id, item_type_id)
);

CREATE TYPE volume_measurement AS ENUM (
    'MILLILITERS',
    'LITERS',
    'GRAMS',
    'KILOGRAMS',
    'TEASPOONS',
    'TABLESPOONS',
    'FLUID_OUNCES',
    'CUPS',
    'PINTS',
    'QUARTS',
    'GALLONS',
    'OUNCES',
    'POUNDS'
);

CREATE TYPE item_kind AS ENUM (
    'SERIALIZED_ASSET',
    'PACKAGED_CONSUMABLE',
    'BULK_CONSUMABLE'
);

CREATE TYPE serialized_item_status AS ENUM (
    'AVAILABLE',
    'IN_USE',
    'RETIRED',
    'TRANSFERRED'
);

ALTER TABLE item_type
    ADD COLUMN item_kind item_kind NOT NULL;

CREATE INDEX idx_item_type_item_kind_name
    ON item_type(item_kind, name);

CREATE TABLE location_item_policy(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    item_type_id INT NOT NULL,
    max INT,
    min INT,
    reorder_point INT,
    reorder_quantity INT,
    CONSTRAINT uq_location_item_policy UNIQUE (location_id, item_type_id)
);

CREATE TABLE location_packaged_stock(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    item_type_id INT NOT NULL,
    nickname VARCHAR(255),
    quantity INT NOT NULL,
    CONSTRAINT uq_location_packaged_stock UNIQUE (location_id, item_type_id),
    CONSTRAINT ck_location_packaged_stock_quantity_nonnegative CHECK (quantity >= 0)
);

CREATE TABLE location_bulk_stock(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    item_type_id INT NOT NULL,
    nickname VARCHAR(255),
    volume_ml DECIMAL(12, 6) NOT NULL,
    volume_measurement volume_measurement NOT NULL,
    CONSTRAINT uq_location_bulk_stock UNIQUE (location_id, item_type_id),
    CONSTRAINT ck_location_bulk_stock_volume_nonnegative CHECK (volume_ml >= 0)
);

CREATE TABLE serialized_item(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    item_type_id INT NOT NULL,
    nickname VARCHAR(255),
    serial_number VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    model VARCHAR(255),
    purchase_price DECIMAL(10, 2),
    status serialized_item_status NOT NULL DEFAULT 'AVAILABLE',
    purchased_at TIMESTAMP,
    notes TEXT,
    CONSTRAINT uq_serialized_item_serial_number UNIQUE (serial_number),
    CONSTRAINT ck_serialized_item_purchase_price_nonnegative CHECK (purchase_price IS NULL OR purchase_price >= 0)
);

CREATE TABLE request_item(
    request_id INT NOT NULL,
    item_type_id INT NOT NULL,
    quantity INT,
    PRIMARY KEY(request_id, item_type_id)
);

CREATE TABLE category(
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE item_category(
    item_type_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY(item_type_id, category_id)
);

ALTER TABLE job
    ADD CONSTRAINT fk_job_location FOREIGN KEY (location_id) REFERENCES location(id);

ALTER TABLE job_item
    ADD CONSTRAINT fk_jobitem_job FOREIGN KEY (job_id) REFERENCES job(id),
    ADD CONSTRAINT fk_jobitem_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE user_location
    ADD CONSTRAINT fk_userlocation_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_userlocation_user FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE user_location_role
    ADD CONSTRAINT fk_userlocationrole_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    ADD CONSTRAINT fk_userlocationrole_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_userlocationrole_role FOREIGN KEY (role_id) REFERENCES "role"(id);

ALTER TABLE "user"
    ADD CONSTRAINT fk_user_approle FOREIGN KEY (app_role_id) REFERENCES "role"(id);

ALTER TABLE request
    ADD CONSTRAINT fk_request_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_request_user FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE location_item_policy
    ADD CONSTRAINT fk_locationitempolicy_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_locationitempolicy_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE location_packaged_stock
    ADD CONSTRAINT fk_locationpackagedstock_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_locationpackagedstock_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE location_bulk_stock
    ADD CONSTRAINT fk_locationbulkstock_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_locationbulkstock_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE serialized_item
    ADD CONSTRAINT fk_serializeditem_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_serializeditem_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE request_item
    ADD CONSTRAINT fk_requestitem_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id),
    ADD CONSTRAINT fk_requestitem_request FOREIGN KEY (request_id) REFERENCES request(id);

ALTER TABLE item_category
    ADD CONSTRAINT fk_itemcategory_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id),
    ADD CONSTRAINT fk_itemcategory_category FOREIGN KEY (category_id) REFERENCES category(id);

CREATE INDEX idx_location_item_policy_location_item_type
    ON location_item_policy(location_id, item_type_id);

CREATE INDEX idx_location_packaged_stock_location_item_type
    ON location_packaged_stock(location_id, item_type_id);

CREATE INDEX idx_location_bulk_stock_location_item_type
    ON location_bulk_stock(location_id, item_type_id);

CREATE INDEX idx_serialized_item_location_item_type
    ON serialized_item(location_id, item_type_id);

-- Phase 3.1 unified inventory read model for front-end list rendering
DROP VIEW IF EXISTS inventory_overview;

CREATE VIEW inventory_overview AS
SELECT
    s.location_id,
    s.item_type_id,
    it.name AS item_name,
    it.item_kind,
    s.quantity,
    NULL::DECIMAL(12, 6) AS volume_ml,
    NULL::volume_measurement AS volume_measurement,
    NULL::VARCHAR(255) AS serial_number,
    NULL::serialized_item_status AS status
FROM location_packaged_stock s
JOIN item_type it ON it.id = s.item_type_id

UNION ALL

SELECT
    s.location_id,
    s.item_type_id,
    it.name AS item_name,
    it.item_kind,
    NULL::INT AS quantity,
    s.volume_ml,
    s.volume_measurement,
    NULL::VARCHAR(255) AS serial_number,
    NULL::serialized_item_status AS status
FROM location_bulk_stock s
JOIN item_type it ON it.id = s.item_type_id

UNION ALL

SELECT
    s.location_id,
    s.item_type_id,
    it.name AS item_name,
    it.item_kind,
    1 AS quantity,
    NULL::DECIMAL(12, 6) AS volume_ml,
    NULL::volume_measurement AS volume_measurement,
    s.serial_number,
    s.status
FROM serialized_item s
JOIN item_type it ON it.id = s.item_type_id;

-- Phase 3.2 access-scoped read pattern
-- Query pattern:
-- 1) Build readable location ids for one user (optionally include app admin global access).
-- 2) Join readable locations to inventory_overview to return a single unified list.
--
-- Sample SQL (read-only template):
-- WITH readable_locations AS (
--     SELECT ul.location_id
--     FROM user_location ul
--     WHERE ul.user_id = :user_id
--
--     UNION
--
--     SELECT ulr.location_id
--     FROM user_location_role ulr
--     JOIN "role" r ON r.id = ulr.role_id
--     WHERE ulr.user_id = :user_id
--       AND r.scope = 'LOCATION'
--       AND r."role" IN ('LOCATION_VIEWER', 'LOCATION_USER', 'LOCATION_ADMIN')
--
--     UNION
--
--     SELECT l.id
--     FROM location l
--     JOIN "user" u ON u.id = :user_id
--     JOIN "role" app_r ON app_r.id = u.app_role_id
--     WHERE app_r.scope = 'APP'
--       AND app_r."role" = 'ROLE_ADMIN'
-- )
-- SELECT
--     io.location_id,
--     io.item_type_id,
--     io.item_name,
--     io.item_kind,
--     io.quantity,
--     io.volume_ml,
--     io.volume_measurement,
--     io.serial_number,
--     io.status
-- FROM inventory_overview io
-- JOIN readable_locations rl ON rl.location_id = io.location_id
-- ORDER BY io.location_id, io.item_name, io.serial_number;

-- Phase 2.4 seed verification queries (read-only)

-- Seeded row counts by table
SELECT 'role' AS table_name, COUNT(*) AS row_count FROM "role"
UNION ALL
SELECT 'user', COUNT(*) FROM "user"
UNION ALL
SELECT 'location', COUNT(*) FROM location
UNION ALL
SELECT 'item_type', COUNT(*) FROM item_type
UNION ALL
SELECT 'category', COUNT(*) FROM category
UNION ALL
SELECT 'item_category', COUNT(*) FROM item_category
UNION ALL
SELECT 'location_item_policy', COUNT(*) FROM location_item_policy
UNION ALL
SELECT 'location_packaged_stock', COUNT(*) FROM location_packaged_stock
UNION ALL
SELECT 'location_bulk_stock', COUNT(*) FROM location_bulk_stock
UNION ALL
SELECT 'serialized_item', COUNT(*) FROM serialized_item;

-- Ensure each inventory table references matching item_kind
SELECT
    'packaged_stock_kind_mismatch' AS check_name,
    COUNT(*) AS mismatch_count
FROM location_packaged_stock s
JOIN item_type it ON it.id = s.item_type_id
WHERE it.item_kind <> 'PACKAGED_CONSUMABLE'
UNION ALL
SELECT
    'bulk_stock_kind_mismatch' AS check_name,
    COUNT(*) AS mismatch_count
FROM location_bulk_stock s
JOIN item_type it ON it.id = s.item_type_id
WHERE it.item_kind <> 'BULK_CONSUMABLE'
UNION ALL
SELECT
    'serialized_item_kind_mismatch' AS check_name,
    COUNT(*) AS mismatch_count
FROM serialized_item s
JOIN item_type it ON it.id = s.item_type_id
WHERE it.item_kind <> 'SERIALIZED_ASSET';

-- Additional integrity checks for serial uniqueness and non-negative values
SELECT
    'serialized_item_duplicate_serial_groups' AS check_name,
    COUNT(*) AS violation_count
FROM (
    SELECT serial_number
    FROM serialized_item
    GROUP BY serial_number
    HAVING COUNT(*) > 1
) dup
UNION ALL
SELECT
    'location_packaged_stock_negative_quantity' AS check_name,
    COUNT(*) AS violation_count
FROM location_packaged_stock
WHERE quantity < 0
UNION ALL
SELECT
    'location_bulk_stock_negative_volume_ml' AS check_name,
    COUNT(*) AS violation_count
FROM location_bulk_stock
WHERE volume_ml < 0
UNION ALL
SELECT
    'serialized_item_negative_purchase_price' AS check_name,
    COUNT(*) AS violation_count
FROM serialized_item
WHERE purchase_price < 0;