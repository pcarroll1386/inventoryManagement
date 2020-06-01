use inventorymanagementdb;

insert into role(`role`)
	values
    ('ROLE_ADMIN'),
    ('ROLE_WAREHOUSE'),
    ('ROLE_USER'),
    ('ROLE_SUPERVISOR');
    
insert into `user`(username, `password`)
	values
    ('admin', 'password');
    
insert into user_role(username, roleId)
	values
    ('admin', 1),
    ('admin', 2),
    ('admin', 3),
    ('admin', 4);
    
update `user` set `password` = '$2a$10$VLtz1k8Dd9LshBFUJnfrLOmu97oqEas8JsmKuWD01VpzokuJ/cPJe' where username = 'Admin';
