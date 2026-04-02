drop database if exists inventorymanagementdbtest;

create database inventorymanagementdbtest;

-- Connect to the new database before running the rest
-- \c inventorymanagementdbtest;

create table "user"(
    username varchar(50) primary key not null,
    "password" varchar(150) not null,
    enabled boolean not null default true,
    supervisorId varchar(50),
    employeeNumber int not null,
    "name" varchar(50) not null
);

create table "role"(
    id serial primary key,
    "role" varchar(50)
);

create table user_role(
    username varchar(50) not null,
    roleId int not null,
    primary key(username, roleId)
);

create table location(
    id serial primary key,
    "name" varchar(50) not null,
    "description" varchar(50),
    template boolean not null default false
);

create table user_location(
    locationId int not null,
    username varchar(50) not null,
    primary key(locationId, username)
);

create table job(
    id serial primary key,
    "name" varchar(25) not null,
    template boolean not null default false,
    locationId int not null
);

create table request(
    id serial primary key,
    locationId int not null,
    username varchar(50) not null,
    submitDate timestamp,
    fillDate timestamp,
    notes text,
    "status" int not null default 0,
    "type" int not null default 0,
    priority int not null default 0,
    workOrder varchar(50)
);

create table item_type(
    id varchar(50) primary key,
    "name" varchar(50) not null,
    nickname varchar(50),
    "description" varchar(200) not null
);

create table job_item(
    jobId int not null,
    itemTypeId varchar(50) not null,
    primary key (jobId, itemTypeId)
);

create table item(
    id serial primary key,
    locationId int not null,
    itemTypeId varchar(50) not null,
    serial_number varchar(255),
    price decimal(8, 2),
    max int,
    min int
);

create table request_item(
    requestId int not null,
    itemTypeId varchar(50) not null,
    quantity int,
    primary key(requestId, itemTypeId)
);

create table category(
    id serial primary key,
    "name" varchar(50) not null
);

create table item_category(
    itemTypeId varchar(50) not null,
    categoryId int not null,
    primary key(itemTypeId, categoryId)
);

alter table job
    add constraint fk_job_location foreign key (locationId) references location(id);

alter table job_item
    add constraint fk_jobitem_job foreign key (jobId) references job(id),
    add constraint fk_jobitem_itemtype foreign key (itemTypeId) references item_type(id);

alter table "user"
    add constraint fk_user_supervisor foreign key (supervisorId) references "user"(username);

alter table user_location
    add constraint fk_userlocation_location foreign key (locationId) references location(id),
    add constraint fk_userlocation_user foreign key (username) references "user"(username);

alter table user_role
    add constraint fk_userrole_user foreign key (username) references "user"(username),
    add constraint fk_userrole_role foreign key (roleId) references "role"(id);

alter table request
    add constraint fk_request_location foreign key (locationId) references location(id),
    add constraint fk_request_user foreign key (username) references "user"(username);

alter table item
    add constraint fk_item_location foreign key (locationId) references location(id),
    add constraint fk_item_itemtype foreign key (itemTypeId) references item_type(id);

alter table request_item
    add constraint fk_requestitem_itemtype foreign key (itemTypeId) references item_type(id),
    add constraint fk_requestitem_request foreign key (requestId) references request(id);

alter table item_category
    add constraint fk_itemcategory_itemtype foreign key (itemTypeId) references item_type(id),
    add constraint fk_itemcategory_category foreign key (categoryId) references category(id); 