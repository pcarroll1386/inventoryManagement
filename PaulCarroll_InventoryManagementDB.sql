drop database if exists inventorymanagementdbtest;

create database inventorymanagementdbtest;

use inventorymanagementdbtest;

create table user(
	username varchar(50) primary key not null,
    `password` varchar(150) not null,
    enabled boolean not null default 1,
    supervisorId varchar(50)
    );
    
create table `role`(
	id int primary key auto_increment,
    `role` varchar(50)
    );

create table user_role(
	username varchar(50) not null,
    roleId int not null,
    primary key(username, roleId)
    );

create table location(
	id int primary key auto_increment,
	`name` varchar(50) not null,
    `description` varchar(50),
    template boolean not null default 0
    );
    
create table user_location(
	locationId int not null,
    username varchar(50) not null,
    primary key(locationId, username)
    );
    
create table job(
	id int primary key auto_increment,
    `name` varchar(25) not null,
    template boolean not null default 0,
    locationId int not null
    );
    
create table request(
	id int primary key auto_increment,
    locationId int not null,
    submitDate datetime,
	fillDate datetime,
    notes text,
    `status` int not null default 0,
    `type` int not null default 0,
    priority int not null default 0,
    workOrder varchar(50)
    );

create table item(
	id varchar(50) primary key,
    `name` varchar(50) not null,
    nickname varchar(50),
    `description` varchar(200) not null,
    price decimal(8,2)
    );
    
create table job_item(
	jobId int not null,
    itemId varchar(50) not null,
    primary key (jobId, itemId)
    );
    
create table location_item(
	locationId int not null,
    itemId varchar(50) not null,
    inInventory int,
    max int,
    min int,
    primary key(locationId, itemId)
    );

create table request_item(
	requestId int not null,
    itemId varchar(50) not null,
    quantity int,
    primary key(requestId, itemId)
    );
    
create table category(
	id int primary key auto_increment,
    `name` varchar(50) not null
    );
    
create table item_category(
	itemId varchar(50) not null,
    categoryId int not null,
    primary key(itemId, categoryId)
    );
    
alter table job
	add constraint foreign key (locationId) references location(id);
    
alter table job_item
	add constraint foreign key (jobId) references job(id),
    add constraint foreign key (itemId) references item(id);
    
alter table `user`
	add constraint foreign key (supervisorId) references `user`(username);
    
alter table user_location
	add constraint foreign key (locationId) references location(id),
    add constraint foreign key (username) references `user`(username);

alter table user_role
	add constraint foreign key (username) references `user`(username),
	add constraint foreign key (roleId) references `role`(id);
    
alter table request
	add constraint foreign key (locationId) references location(id);
    
alter table location_item
	add constraint foreign key (locationId) references location(id),
	add constraint foreign key (itemId) references item(id);
    
alter table request_item
	add constraint foreign key (itemId) references item(id),
	add constraint foreign key (requestId) references request(id);
    
alter table item_category
	add constraint foreign key (itemId) references item(id),
	add constraint foreign key (categoryId) references category(id);