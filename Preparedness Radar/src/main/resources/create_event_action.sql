drop table if exists Action cascade;
create table Action (
	id bigserial not null,
	name varchar(255),
	primary key (id)
);

drop table if exists Event cascade;
create table Event (
	id  bigserial not null,
	color int4 not null,
	name varchar(255),
	visible boolean not null,
	event_id int8,
	primary key (id)
); 

alter table Event 
	add constraint event_name_constraint unique (name);

alter table Event 
	add constraint child_parent_constraint
	foreign key (event_id) references Event;