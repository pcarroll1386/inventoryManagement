use inventorymanagementdbtest;

insert into role(`role`, scope)
	values
    ('ROLE_ADMIN', 'APP'),
    ('ROLE_WAREHOUSE', 'APP'),
    ('ROLE_USER', 'APP'),
    ('ROLE_MANAGER', 'APP'),
    ('LOCATION_ADMIN', 'LOCATION'),
    ('LOCATION_USER', 'LOCATION'),
    ('LOCATION_VIEWER', 'LOCATION');
    
insert into `user`(username, `password`, `name`, employee_identification, app_role_id, enabled)
	values
    ('admin', 'password', 'Admin', '1', 1, true);
    
update `user` set `password` = '$2a$10$VLtz1k8Dd9LshBFUJnfrLOmu97oqEas8JsmKuWD01VpzokuJ/cPJe' where username = 'admin';
