-- Run this section separately in DBeaver (connected to postgres DB), then reconnect to inventorymanagementdbtest.
DROP DATABASE IF EXISTS inventorymanagementdbtest;
CREATE DATABASE inventorymanagementdbtest;

-- After reconnecting to inventorymanagementdbtest, run everything below.

CREATE TABLE "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    supervisor_id VARCHAR(255),
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

CREATE TABLE user_role(
    user_id int NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY(user_id, role_id)
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
    nickname VARCHAR(255),
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

CREATE TABLE item(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    item_type_id INT NOT NULL,
    serial_number VARCHAR(255),
    price DECIMAL(8, 2),
    max INT,
    min INT,
    quantity INT,
    volume_ml DECIMAL(12, 6),
    volume_measurement volume_measurement,
    is_bulk BOOLEAN NOT NULL DEFAULT FALSE
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

ALTER TABLE "user"
    ADD CONSTRAINT fk_user_supervisor FOREIGN KEY (supervisor_id) REFERENCES "user"(id);

ALTER TABLE user_location
    ADD CONSTRAINT fk_userlocation_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_userlocation_user FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE user_location_role
    ADD CONSTRAINT fk_userlocationrole_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    ADD CONSTRAINT fk_userlocationrole_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_userlocationrole_role FOREIGN KEY (role_id) REFERENCES "role"(id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_userrole_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    ADD CONSTRAINT fk_userrole_role FOREIGN KEY (role_id) REFERENCES "role"(id);

ALTER TABLE request
    ADD CONSTRAINT fk_request_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_request_user FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE item
    ADD CONSTRAINT fk_item_location FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT fk_item_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id);

ALTER TABLE request_item
    ADD CONSTRAINT fk_requestitem_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id),
    ADD CONSTRAINT fk_requestitem_request FOREIGN KEY (request_id) REFERENCES request(id);

ALTER TABLE item_category
    ADD CONSTRAINT fk_itemcategory_itemtype FOREIGN KEY (item_type_id) REFERENCES item_type(id),
    ADD CONSTRAINT fk_itemcategory_category FOREIGN KEY (category_id) REFERENCES category(id);