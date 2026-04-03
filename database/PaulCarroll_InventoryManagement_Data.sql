use inventorymanagementdbtest;

insert into role(`role`)
	values
    ('ROLE_ADMIN'),
    ('ROLE_WAREHOUSE'),
    ('ROLE_USER'),
    ('ROLE_MANAGER');
    
insert into `user`(username, `password`, `name`, employeeNumber)
	values
    ('admin', 'password', 'Admin', 1);
    
insert into user_role(username, roleId)
	values
    ('admin', 1),
    ('admin', 2),
    ('admin', 3),
    ('admin', 4);
    
update `user` set `password` = '$2a$10$VLtz1k8Dd9LshBFUJnfrLOmu97oqEas8JsmKuWD01VpzokuJ/cPJe' where username = 'admin';
