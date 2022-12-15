insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Mirko', 'Preradovic', 'profilna', '+38121521', 'mirko@gmail.com', 'Mirko123', false, true, 'Rajfajzenova 12, Novi Sad');
insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Marko', 'Kraljevic', 'profilna', '+38121521', 'marko@gmail.com', 'Marko123', false, true, 'Mirkova 14, Novi Sad');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Nemanja', 'Stefanovic', 'profilna', '+38121521', 'nemus@gmail.com', 'Nemanja123', false, true, 'Radnicka 20, Novi Sad');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Marko', 'Preradovic', 'profilna', '+38121521', 'markopreradovic@gmail.com', 'Marko123', false, true, 'Kurtijeva 12, Novi Sad');



insert into Passenger(id, is_confirmed_mail, amount_of_money) values(3, true, 1000);
insert into Passenger(id, is_confirmed_mail, amount_of_money) values(4, true, 3000);

insert into Admin(name, password, profile_picture, surname, username)
            values('Nikolaj', 'Nikolaj123', 'U3dhZ2dlciByb2Nrcw', 'Velimirovic', 'nikolaj');


insert into Vehicle_Type(price_per_km, name) values(120, 'STANDARD');
insert into Vehicle_Type(price_per_km, name) values(140, 'LUXURY');
insert into Vehicle_Type(price_per_km, name) values(130, 'VAN');

insert into location(id, address, latitude, longitude) values(1, 'Radnicka 19, Novi Sad', 19.55, 17.55);

insert into Vehicle
    (id, is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
    values
    (1, true, true, 'Audi A3', 4, 'KK077KK', 1, 1);

insert into Drivers(id) values(1);
insert into Drivers(id, vehicle_id) values(2, 1);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 1);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Vozacka Dozvola', 2);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 2);



