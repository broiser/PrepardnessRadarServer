alter table Event 
	drop constraint FK_57p3clpxspk3lfdxjoo6d5rg1;
alter table FeatureEvoluation 
    drop constraint FK_on73kr37qev50lg1ls4s4c5o;
alter table FeatureEvoluation 
    drop constraint FK_gernoqweaxpi9b5a0acw2ksqi;
drop table if exists Action cascade
drop table if exists Event cascade;
drop table if exists Feature cascade;
drop table if exists FeatureEvoluation cascade;

create table Action (
    id  bigserial not null,
    name varchar(255),
    primary key (id)
);

create table Event (
    id  bigserial not null,
    color int4 not null,
    name varchar(255),
    visible boolean not null,
    event_id int8,
    primary key (id)
);

create table Feature (
    id  bigserial not null,
    geometry GEOMETRY,
    primary key (id)
);
 
create table FeatureEvoluation (
    id  bigserial not null,
    date timestamp,
    event_id int8,
    feature_id int8,
    status char,
    primary key (id)
);

alter table Event 
    add constraint UK_ij7n685n8qbung3jvhw3rifm7  unique (name);

alter table Event 
    add constraint FK_57p3clpxspk3lfdxjoo6d5rg1 
    foreign key (event_id) 
    references Event;

alter table FeatureEvoluation 
	add constraint FK_on73kr37qev50lg1ls4s4c5o 
	foreign key (event_id) 
	references Event;
 
alter table FeatureEvoluation 
	add constraint FK_gernoqweaxpi9b5a0acw2ksqi 
	foreign key (feature_id) 
	references Feature;