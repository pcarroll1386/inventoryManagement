drop table serial_number cascade;

create table public.item(
	id uuid primary key default gen_random_uuid(),
	name varchar(255) not null,
	model_number varchar(255)
);

create table public.category(
	id uuid primary key default gen_random_uuid(),
	name varchar(255) not null unique
);


create table public.item_category(
	category_id uuid not null,
	item_id uuid not null,
	primary key(category_id, item_id),
	CONSTRAINT fk_category foreign key (category_id) references category(id),
	CONSTRAINT fk_item foreign key (item_id) references item(id)
);

create table public.location_item(
	id uuid primary key default gen_random_uuid(),
	item_id uuid not null,
	location_id uuid not null,
	nickname varchar(255),
	description text,
	price numeric(10, 2) not null default 0.00,
	quantity int not null default 0,
	max int,
	min int,
	CONSTRAINT fk_item foreign key (item_id) references item(id),
	CONSTRAINT fk_location foreign key (location_id) references location(id)
);


create table public.serial_number(
	serial_number varchar(255) primary key,
	item_id uuid not null,
	CONSTRAINT fk_location_item foreign key (location_item_id) references location_item(id)
);



--create table public.location(
--	id uuid primary key default gen_random_uuid(),
--	name varchar(255) not null,
--	description text,
--	template bool not null default false
--);
--
--create table public.location_item(
--	id uuid primary key default gen_random_uuid(),
--	location_id uuid not null,
--	item_id uuid not null,
--	CONSTRAINT fk_location foreign key (location_id) references location(id),
--	CONSTRAINT fk_item foreign key (item_id) references item(id)
--);
--
--
--create table public.user(
--	username varchar(255) primary key,
--	password varchar(255) not null,
--	enabled boolean not null
--);
--
--create table public.role(
--	id SERIAL primary key,
--	role varchar(255) not null	
--);

    
--create table public.user_role(
--	username varchar(255) not null,
--	role_id int not null,
--	primary key(username, role_id),
--	CONSTRAINT fk_role foreign key (role_id) references role(id),
--	CONSTRAINT fk_user foreign key (username) references public.user(username)
--);