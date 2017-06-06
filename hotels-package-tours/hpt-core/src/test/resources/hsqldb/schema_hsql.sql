
    alter table Hotel 
       drop constraint FKbgfk00jcxkj4o5hikigfvaqf4;

    alter table Package 
       drop constraint FK7t9lth36cr39teqtmynkctm28;

    drop table Destination if exists;

    drop table Hotel if exists;

    drop table Package if exists;

    drop sequence hibernate_sequence if exists;
create sequence hibernate_sequence start with 1 increment by 1;

    create table Destination (
       id bigint not null,
        lowestPrice double,
        name varchar(255) not null,
        primary key (id)
    );

    create table Hotel (
       id bigint not null,
        active boolean not null,
        lowestPrice double,
        name varchar(255) not null,
        destinationId bigint not null,
        primary key (id)
    );

    create table Package (
       id bigint not null,
        active boolean not null,
        arrival timestamp not null,
        departure timestamp not null,
        price double not null,
        hotelId bigint not null,
        primary key (id)
    );

    alter table Destination 
       add constraint UK_4355fgxcietdl3g85oq0o0h07 unique (name);

    alter table Hotel 
       add constraint UK_tkn1uf35ihw6gsyq6ba5icq6x unique (name);

    alter table Hotel 
       add constraint FKbgfk00jcxkj4o5hikigfvaqf4 
       foreign key (destinationId) 
       references Destination;

    alter table Package 
       add constraint FK7t9lth36cr39teqtmynkctm28 
       foreign key (hotelId) 
       references Hotel;
