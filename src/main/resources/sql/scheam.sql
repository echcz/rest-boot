create table if not exists user
(
	id int auto_increment
		primary key,
	username varchar(31) not null comment '用户名',
	password char(64) not null comment '密码',
	state tinyint default 1 not null comment '1启用2禁用',
	constraint user_username_UN
		unique (username)
)
comment '用户';


create table if not exists role
(
	id int auto_increment
		primary key,
	name varchar(15) not null comment '角色名',
	state tinyint default 1 not null comment '1启用2禁用',
	remark varchar(100) null comment '说明',
	constraint role_name_UN
		unique (name)
)
comment '角色';


create table if not exists permission
(
	id int auto_increment
		primary key,
	name varchar(31) not null comment '名称',
	method varchar(7) not null comment 'REST请求方法:*表示任意方法',
	uri varchar(127) not null comment 'URI:*匹配一层,**匹配多层',
	remark varchar(100) null comment '说明',
	constraint permission_UN
		unique (method, uri)
)
comment '权限';


create table if not exists user_role
(
	user_id int not null comment '用户id',
	role_id int not null comment '角色id',
	primary key (user_id, role_id)
)
comment '用户-角色关联';


create table if not exists permission_role
(
	permission_id int not null comment '权限id',
	role_id int not null comment '角色id',
	primary key (permission_id, role_id)
)
comment '权限-角色关联';

