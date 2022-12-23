insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Mirko', 'Preradovic', 'profilna', '+38121521', 'mirko@gmail.com', '$2a$12$C6YIMN3gkgH5WPCAps9PNuiHHntv9Mv4DZSvkPU1Z9sCPGimd6VpO', false, true, 'Rajfajzenova 12, Novi Sad', 'DRIVER');
insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Marko', 'Kraljevic', 'profilna', '+38121521', 'marko@gmail.com', '$2a$12$Ye8v/2SDCbfLKpQskOEjzOibBaPppIb3FRlvTUcZC69we3vTd69T6
', false, true, 'Mirkova 14, Novi Sad', 'DRIVER');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Nemanja', 'Stefanovic', 'profilna', '+38121521', 'nemus@gmail.com', '$2a$12$cBC2hAENKfpv/W475UJApOJpmYtd2fidX/PbOSvkpXoHsBD2Isxc2
', false, true, 'Radnicka 20, Novi Sad', 'PASSENGER');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Marko', 'Preradovic', 'profilna', '+38121521', 'markopreradovic@gmail.com', '$2a$12$91FlcSvH0BsWdp6PlgYFUOTji1YDAlTKk/tECGcDQ6HepgyU3X8z6
', false, true, 'Kurtijeva 12, Novi Sad', 'PASSENGER');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address)
values('Nebojsa', 'Vuga', 'profilna', '+38fsa1521', 'nebojsavuga@gmail.com', '$2a$12$BI54V4pht5okSY6VUnpul.Qd4X/Uc5/1IdIsWDInuXf/ra7JiANBC
', false, true, 'Radnicka 19, Novi Sad');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
    values('Nikolaj', 'Velimirovic', 'profilna', '+38fsa1521', 'nikolaj@gmail.com', '$2a$12$ArvLiFrS3Xx3UoEvcNEpbegAIe8nqGoha7K.zHM9JUQaoRKeU4TDG', false, true, 'Radnicka 19, Novi Sad', 'ADMIN');



insert into Passenger(id, is_confirmed_mail, amount_of_money) values(3, true, 1000);
insert into Passenger(id, is_confirmed_mail, amount_of_money) values(4, true, 3000);

insert into Vehicle_Type(price_per_km, name) values(120, 'STANDARD');
insert into Vehicle_Type(price_per_km, name) values(140, 'LUXURY');
insert into Vehicle_Type(price_per_km, name) values(130, 'VAN');

insert into location(id, address, latitude, longitude) values(1, 'Radnicka 19, Novi Sad', 45.249101856630546, 19.848034);
insert into location(id, address, latitude, longitude) values(2, 'Zeleznicka 2, Novi Sad', 45.2493924092008, 19.840783700998372 );
insert into location(id, address, latitude, longitude) values(3, 'Cerska 34, Lacarak', 44.997668, 19.557723 );

insert into Vehicle
    (id, is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
    values
    (1, true, true, 'Audi A3', 4, 'KK077KK', 1, 1);

insert into Vehicle
(id, is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
values
(2, true, true, 'Opel Astra G', 4, 'SM333RK', 2, 1);

insert into Vehicle
(id, is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
values
(3, true, true, 'Mecedes Benz C class', 4, 'SM077CF', 3, 2);

insert into Drivers(id, vehicle_id) values(1, 2);
insert into Drivers(id, vehicle_id) values(2, 1);
insert into Drivers(id, vehicle_id) values(5, 3);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 1);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Vozacka Dozvola', 2);
insert into Document(document_image, name, driver_id)
values ('U3dhZ2dlciByb2Nrcw==', 'Saobracajna Dozvola', 2);



