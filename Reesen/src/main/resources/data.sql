insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
            values('Marko', 'Kraljevic', 'profilna', '+38121521', 'marko@gmail.com', 'Marko123', false, true, 'Rajfajzenova 12, Novi Sad');
insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Mirko', 'Rasevic', 'profilna', '+38121521', 'mirko@gmail.com', 'Mirko123', false, true, 'Mirkova 14, Novi Sad');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Nemanja', 'Stefanovic', 'profilna', '+38121521', 'nemus@gmail.com', 'Nemanja123', false, true, 'Radnicka 20, Novi Sad');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Marko', 'Preradovic', 'profilna', '+38121521', 'markopreradovic@gmail.com', 'Marko123', false, true, 'Kurtijeva 12, Novi Sad');

insert into Drivers(id) values(1);
insert into Drivers(id) values(2);


insert into Document(document_image, name, driver_id)
            values ('U3dhZ2dlciByb2Nrcw==', 'Vozacka Dozvola', 1);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 1);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Vozacka Dozvola', 2);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 2);


insert into Vehicle_Type(price_per_km, name) values(120, 'STANDARD');
insert into Vehicle_Type(price_per_km, name) values(140, 'LUXURY');
insert into Vehicle_Type(price_per_km, name) values(130, 'VAN');