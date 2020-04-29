drop database if exists inventorymanagementdbtest;

create database inventorymanagementdbtest;

use inventorymanagementdbtest;

create table user(
	username varchar(50) primary key not null,
    `password` varchar(150) not null,
    enabled boolean not null default 1 
    );
    
create table `role`(
	id int primary key auto_increment,
    `name` varchar(50)
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
    username varchar(50)
    );
    
create table request(
	id int primary key auto_increment,
    requestDate datetime not null,
    `status` int not null default 0,
    locationId int not null
    );

create table item(
	id int primary key auto_increment,
    `name` varchar(50) not null,
    nickname varchar(50),
    `description` varchar(200) not null,
    price decimal(8,2)
    );
    
create table location_item(
	locationId int not null,
    itemId int not null not null,
    inInventory int not null default 0,
    max int,
    min int,
    primary key(locationId, itemId)
    );

create table request_item(
	requestId int not null,
    itemId int not null,
    quantity int,
    primary key(requestId, itemId)
    );
    
create table category(
	id int primary key auto_increment,
    `name` varchar(50) not null
    );
    
create table item_category(
	itemId int not null,
    categoryId int not null,
    primary key(itemId, categoryId)
    );
    
alter table location
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