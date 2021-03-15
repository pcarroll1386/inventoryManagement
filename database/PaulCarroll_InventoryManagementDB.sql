DROP DATABASE IF EXISTS `inventorydb`;

CREATE DATABASE `inventorydb`;

USE 'inventorydb';

CREATE TABLE user(
    username VARCHAR(50) PRIMARY KEY NOT NULL,
    `password` VARCHAR(150) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT 1,
    supervisorId VARCHAR(50),
    employeeNumber INT NOT NULL,
    `name` VARCHAR(50) NOT NULL
);

CREATE TABLE `role`(
    id INT PRIMARY KEY auto_increment,
    `role` VARCHAR(50)
);

CREATE TABLE user_role(
    username VARCHAR(50) NOT NULL,
    roleId INT NOT NULL,
    PRIMARY KEY (username, roleId)
);

CREATE TABLE location(
    id INT PRIMARY KEY auto_increment,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(50),
    template BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE user_location(
    locationId INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    PRIMARY KEY (locationId, username)
);

CREATE TABLE job(
    id INT PRIMARY KEY auto_increment,
    `name` VARCHAR(25) NOT NULL,
    template BOOLEAN NOT NULL DEFAULT 0,
    locationId INT NOT NULL
);

CREATE TABLE request(
    id INT PRIMARY KEY auto_increment,
    locationId INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    submitDate datetime,
    fillDate datetime,
    notes TEXT,
    `status` INT NOT NULL DEFAULT 0,
    `type` INT NOT NULL DEFAULT 0,
    priority INT NOT NULL DEFAULT 0,
    workOrder VARCHAR(50)
);

CREATE TABLE item(
    id VARCHAR(50) PRIMARY KEY ,
    `name` VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    `description` VARCHAR(200) NOT NULL,
    price decimal(8, 2)
);

CREATE TABLE job_item(
    jobId INT NOT NULL,
    itemId VARCHAR(50) NOT NULL,
    PRIMARY KEY (jobId, itemId)
);

CREATE TABLE serial_number(
    id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE location_item(
    id INT PRIMARY KEY auto_increment,
    locationId INT NOT NULL,
    itemId VARCHAR(50) NOT NULL,
    inInventory INT,
    max INT,
    min INT
);

CREATE TABLE item_serial_number (
    locationItemId INT NOT NULL,
    serialNumberId VARCHAR(255) NOT NULL,
    PRIMARY KEY (locationItemId, serialNumberId)
);

CREATE TABLE request_item(
    requestId INT NOT NULL,
    itemId VARCHAR(50) NOT NULL,
    quantity INT,
    PRIMARY KEY (requestId, itemId)
);

CREATE TABLE category(
    id INT PRIMARY KEY auto_increment,
    `name` VARCHAR(50) NOT NULL
);

CREATE TABLE item_category(
    itemId VARCHAR(50) NOT NULL,
    categoryId INT NOT NULL,
    PRIMARY KEY (itemId, categoryId)
);

ALTER TABLE
    job
ADD CONSTRAINT FOREIGN KEY (locationId) REFERENCES location(id);

ALTER TABLE
    job_item
ADD CONSTRAINT FOREIGN KEY (jobId) REFERENCES job(id),
ADD CONSTRAINT FOREIGN KEY (itemId) REFERENCES item(id);

ALTER TABLE
    `user`
ADD CONSTRAINT FOREIGN KEY (supervisorId) REFERENCES `user`(username);

ALTER TABLE
    user_location
ADD CONSTRAINT FOREIGN KEY (locationId) REFERENCES location(id),
ADD CONSTRAINT FOREIGN KEY (username) REFERENCES `user`(username);

ALTER TABLE
    user_role
ADD CONSTRAINT FOREIGN KEY (username) REFERENCES `user`(username),
ADD CONSTRAINT FOREIGN KEY (roleId) REFERENCES `role`(id);

ALTER TABLE
    request
ADD CONSTRAINT FOREIGN KEY (locationId) REFERENCES location(id),
ADD CONSTRAINT FOREIGN KEY (username) REFERENCES `user`(username);

ALTER TABLE
    location_item
ADD CONSTRAINT FOREIGN KEY (locationId) REFERENCES location(id),
ADD CONSTRAINT FOREIGN KEY (itemId) REFERENCES item(id);

ALTER TABLE
    request_item
ADD CONSTRAINT FOREIGN KEY (itemId) REFERENCES item(id),
ADD CONSTRAINT FOREIGN KEY (requestId) REFERENCES request(id);

ALTER TABLE
    item_category
ADD CONSTRAINT FOREIGN KEY (itemId) REFERENCES item(id),
ADD CONSTRAINT FOREIGN KEY (categoryId) REFERENCES category(id);

ALTER TABLE
    item_serial_number
ADD CONSTRAINT FOREIGN KEY (locationItemId) REFERENCES location_item(id),
ADD CONSTRAINT FOREIGN KEY (serialNumberId) REFERENCES serial_number(id);
