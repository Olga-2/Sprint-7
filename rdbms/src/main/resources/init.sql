--liquibase formatted sql

--changeset olga:init

create table accounts1
(
    id bigserial constraint account_pk primary key,
    amount int CHECK (amount >= 0),
    version int
)

--changeset olga:update
CREATE INDEX account_ind ON accounts1 (id)

--changeset olga:insert
Insert into accounts1 (id, amount) values (1, 100);
Insert into accounts1 (id, amount) values (2, 1200);
Insert into accounts1 (id, amount) values (3, 700);
Insert into accounts1 (id, amount) values (4, 1700);


