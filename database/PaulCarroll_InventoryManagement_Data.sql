-- Phase 2 bootstrap seed data (PostgreSQL, idempotent)

-- App and location roles
INSERT INTO "role" ("role", scope)
VALUES
    ('ROLE_ADMIN', 'APP'),
    ('ROLE_WAREHOUSE', 'APP'),
    ('ROLE_USER', 'APP'),
    ('ROLE_MANAGER', 'APP'),
    ('LOCATION_ADMIN', 'LOCATION'),
    ('LOCATION_USER', 'LOCATION'),
    ('LOCATION_VIEWER', 'LOCATION')
-- If the role already exists, keep it and refresh scope from seed data.
ON CONFLICT ("role") DO UPDATE
SET scope = EXCLUDED.scope;

-- Default app admin user (bcrypt hash)
INSERT INTO "user" (username, "password", "name", employee_identification, app_role_id, enabled)
SELECT
    'admin',
    '$2a$10$VLtz1k8Dd9LshBFUJnfrLOmu97oqEas8JsmKuWD01VpzokuJ/cPJe',
    'Application Admin',
    'EMP-0001',
    r.id,
    TRUE
FROM "role" r
WHERE r."role" = 'ROLE_ADMIN'
-- If admin already exists, refresh all managed fields (including password hash).
ON CONFLICT (username) DO UPDATE
SET
    "password" = EXCLUDED."password",
    "name" = EXCLUDED."name",
    employee_identification = EXCLUDED.employee_identification,
    app_role_id = EXCLUDED.app_role_id,
    enabled = EXCLUDED.enabled;

-- Seed locations
INSERT INTO location ("name", "description", template)
SELECT v."name", v."description", v.template
FROM (
    VALUES
        ('Main Warehouse', 'Primary receiving and storage location', FALSE),
        ('Kitchen', 'Consumables storage and prep area', FALSE)
) AS v("name", "description", template)
WHERE NOT EXISTS (
    SELECT 1
    FROM location l
    WHERE l."name" = v."name"
);

-- Link admin to all seeded locations
INSERT INTO user_location (location_id, user_id)
SELECT l.id, u.id
FROM location l
JOIN "user" u ON u.username = 'admin'
WHERE l."name" IN ('Main Warehouse', 'Kitchen')
-- Relationship table insert: skip duplicates safely.
ON CONFLICT (location_id, user_id) DO NOTHING;

INSERT INTO user_location_role (user_id, location_id, role_id)
SELECT u.id, l.id, r.id
FROM "user" u
JOIN location l ON l."name" IN ('Main Warehouse', 'Kitchen')
JOIN "role" r ON r."role" = 'LOCATION_ADMIN'
WHERE u.username = 'admin'
-- Relationship table insert: skip duplicates safely.
ON CONFLICT (user_id, location_id, role_id) DO NOTHING;

-- Seed item categories
INSERT INTO category ("name")
SELECT v."name"
FROM (
    VALUES
        ('Electronics'),
        ('Furniture'),
        ('Vehicles'),
        ('Food Dry Goods'),
        ('Beverages'),
        ('Cleaning Supplies')
) AS v("name")
WHERE NOT EXISTS (
    SELECT 1
    FROM category c
    WHERE c."name" = v."name"
);

-- Seed item types with explicit item_kind
INSERT INTO item_type ("name", "description", item_kind)
SELECT v."name", v."description", v.item_kind::item_kind
FROM (
    VALUES
        ('Laptop', 'Serialized business laptop asset', 'SERIALIZED_ASSET'),
        ('Office Chair', 'Serialized ergonomic office chair', 'SERIALIZED_ASSET'),
        ('Forklift', 'Serialized warehouse forklift asset', 'SERIALIZED_ASSET'),
        ('Canned Beans Case', '24-can packaged consumable case', 'PACKAGED_CONSUMABLE'),
        ('Bottled Water Case', 'Packaged bottled water case', 'PACKAGED_CONSUMABLE'),
        ('All-Purpose Flour', 'Bulk flour tracked by volume', 'BULK_CONSUMABLE'),
        ('Granulated Sugar', 'Bulk sugar tracked by volume', 'BULK_CONSUMABLE')
) AS v("name", "description", item_kind)
WHERE NOT EXISTS (
    SELECT 1
    FROM item_type it
    WHERE it."name" = v."name"
);

-- Seed item_type-to-category mappings
INSERT INTO item_category (item_type_id, category_id)
SELECT it.id, c.id
FROM (
    VALUES
        ('Laptop', 'Electronics'),
        ('Office Chair', 'Furniture'),
        ('Forklift', 'Vehicles'),
        ('Canned Beans Case', 'Food Dry Goods'),
        ('Bottled Water Case', 'Beverages'),
        ('All-Purpose Flour', 'Food Dry Goods'),
        ('Granulated Sugar', 'Food Dry Goods')
) AS v(item_name, category_name)
JOIN item_type it ON it."name" = v.item_name
JOIN category c ON c."name" = v.category_name
-- Relationship table insert: skip duplicates safely.
ON CONFLICT (item_type_id, category_id) DO NOTHING;

-- Seed min/max/reorder policy per (location, item_type)
INSERT INTO location_item_policy (location_id, item_type_id, max, min, reorder_point, reorder_quantity)
SELECT
    l.id,
    it.id,
    v.max_qty,
    v.min_qty,
    v.reorder_point,
    v.reorder_qty
FROM (
    VALUES
        ('Main Warehouse', 'Canned Beans Case', 400, 100, 150, 200),
        ('Kitchen', 'Canned Beans Case', 120, 20, 40, 80),
        ('Main Warehouse', 'Bottled Water Case', 600, 150, 200, 300),
        ('Kitchen', 'Bottled Water Case', 180, 30, 50, 100),
        ('Main Warehouse', 'All-Purpose Flour', 500000, 100000, 150000, 200000),
        ('Kitchen', 'All-Purpose Flour', 200000, 50000, 70000, 100000),
        ('Main Warehouse', 'Granulated Sugar', 400000, 80000, 120000, 160000),
        ('Kitchen', 'Granulated Sugar', 150000, 30000, 50000, 90000),
        ('Main Warehouse', 'Laptop', 80, 10, 15, 20),
        ('Main Warehouse', 'Office Chair', 120, 20, 30, 40),
        ('Main Warehouse', 'Forklift', 10, 2, 3, 2)
) AS v(location_name, item_name, max_qty, min_qty, reorder_point, reorder_qty)
JOIN location l ON l."name" = v.location_name
JOIN item_type it ON it."name" = v.item_name
-- Policy rows should stay current when seed values change.
ON CONFLICT (location_id, item_type_id) DO UPDATE
SET
    max = EXCLUDED.max,
    min = EXCLUDED.min,
    reorder_point = EXCLUDED.reorder_point,
    reorder_quantity = EXCLUDED.reorder_quantity;

-- Seed packaged stock
INSERT INTO location_packaged_stock (location_id, item_type_id, nickname, quantity)
SELECT
    l.id,
    it.id,
    v.nickname,
    v.quantity
FROM (
    VALUES
        ('Main Warehouse', 'Canned Beans Case', 'Beans Reserve', 220),
        ('Kitchen', 'Canned Beans Case', 'Beans Daily Use', 45),
        ('Main Warehouse', 'Bottled Water Case', 'Water Reserve', 310),
        ('Kitchen', 'Bottled Water Case', 'Water Service Stock', 72)
) AS v(location_name, item_name, nickname, quantity)
JOIN location l ON l."name" = v.location_name
JOIN item_type it ON it."name" = v.item_name
-- Stock rows should stay current when seed values change.
ON CONFLICT (location_id, item_type_id) DO UPDATE
SET
    nickname = EXCLUDED.nickname,
    quantity = EXCLUDED.quantity;

-- Seed bulk stock
INSERT INTO location_bulk_stock (location_id, item_type_id, nickname, volume_ml, volume_measurement)
SELECT
    l.id,
    it.id,
    v.nickname,
    v.volume_ml,
    v.volume_measurement::volume_measurement
FROM (
    VALUES
        ('Main Warehouse', 'All-Purpose Flour', 'Flour Bin A', 240000.000000, 'KILOGRAMS'),
        ('Kitchen', 'All-Purpose Flour', 'Flour Bin Kitchen', 90000.000000, 'KILOGRAMS'),
        ('Main Warehouse', 'Granulated Sugar', 'Sugar Bin A', 180000.000000, 'KILOGRAMS'),
        ('Kitchen', 'Granulated Sugar', 'Sugar Bin Kitchen', 60000.000000, 'KILOGRAMS')
) AS v(location_name, item_name, nickname, volume_ml, volume_measurement)
JOIN location l ON l."name" = v.location_name
JOIN item_type it ON it."name" = v.item_name
-- Stock rows should stay current when seed values change.
ON CONFLICT (location_id, item_type_id) DO UPDATE
SET
    nickname = EXCLUDED.nickname,
    volume_ml = EXCLUDED.volume_ml,
    volume_measurement = EXCLUDED.volume_measurement;

-- Seed serialized inventory
INSERT INTO serialized_item (
    location_id,
    item_type_id,
    nickname,
    serial_number,
    brand,
    model,
    purchase_price,
    status,
    purchased_at,
    notes
)
SELECT
    l.id,
    it.id,
    v.nickname,
    v.serial_number,
    v.brand,
    v.model,
    v.purchase_price,
    v.status::serialized_item_status,
    v.purchased_at,
    v.notes
FROM (
    VALUES
        ('Main Warehouse', 'Laptop', 'Admin Laptop', 'LT-2026-0001', 'Dell', 'Latitude 7440', 1499.99, 'AVAILABLE', TIMESTAMP '2026-01-12 10:00:00', 'Assigned to onboarding pool'),
        ('Main Warehouse', 'Office Chair', 'Front Desk Chair', 'CH-2026-0007', 'Herman Miller', 'Aeron', 899.00, 'IN_USE', TIMESTAMP '2025-11-08 09:15:00', 'Currently in front office'),
        ('Main Warehouse', 'Forklift', 'Loading Dock Forklift', 'FL-2024-0002', 'Toyota', '8FGCU25', 24500.00, 'IN_USE', TIMESTAMP '2024-03-21 13:30:00', 'Dock loading operations')
) AS v(location_name, item_name, nickname, serial_number, brand, model, purchase_price, status, purchased_at, notes)
JOIN location l ON l."name" = v.location_name
JOIN item_type it ON it."name" = v.item_name
-- Serialized items are keyed by serial_number; refresh mutable fields on rerun.
ON CONFLICT (serial_number) DO UPDATE
SET
    location_id = EXCLUDED.location_id,
    item_type_id = EXCLUDED.item_type_id,
    nickname = EXCLUDED.nickname,
    brand = EXCLUDED.brand,
    model = EXCLUDED.model,
    purchase_price = EXCLUDED.purchase_price,
    status = EXCLUDED.status,
    purchased_at = EXCLUDED.purchased_at,
    notes = EXCLUDED.notes;
