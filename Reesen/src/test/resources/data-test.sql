drop ALIAS if exists TO_DATE;
CREATE ALIAS TO_DATE as '
import java.text.*;
@CODE
java.util.Date toDate(String originalDate, String dateFormat) throws Exception {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(originalDate);
}
';


insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Mirko', 'Preradovic', 'profilna', '12421421','mirko@gmail.com', 'Mirko123', false, false, 'Rajfajzenova 12, Novi Sad', 'DRIVER');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)

values('Marko', 'Kraljevic', 'profilna', '1242146', 'marko@gmail.com', 'Marko123', true, true, 'Mirkova 14, Novi Sad', 'DRIVER');


insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Nemanja', 'Stefanovic', 'profilna', '+38121521', 'nemus@gmail.com', 'Nemanja123',  false, false , 'Radnicka 20, Novi Sad', 'PASSENGER');

insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Marko', 'Preradovic', 'profilna', '+38121521', 'markopreradovic@gmail.com', 'Marko123', false, false , 'Kurtijeva 12, Novi Sad', 'PASSENGER');


insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Nebojsa', 'Vuga', 'profilna', '1243124', 'nebojsavuga@gmail.com', 'Nebojsa123', false, false , 'Radnicka 19, Novi Sad', 'PASSENGER');


insert into Users(name, surname, profile_picture, telephone_number, email, password, is_blocked, is_active, address, role)
values('Nikolaj', 'Velimirovic', 'profilna', '124124214', 'nikolaj@gmail.com', 'Nikolaj123', false, false, 'Radnicka 19, Novi Sad', 'ADMIN');


insert into Passenger(id, is_confirmed_mail, amount_of_money) values(3, true, 1000);
insert into Passenger(id, is_confirmed_mail, amount_of_money) values(4, true, 3000);
insert into Passenger(id, is_confirmed_mail, amount_of_money) values(5, true, 3000);

insert into Vehicle_Type(price_per_km, name) values(120, 'STANDARD');
insert into Vehicle_Type(price_per_km, name) values(140, 'LUXURY');
insert into Vehicle_Type(price_per_km, name) values(130, 'VAN');

insert into location(address, latitude, longitude) values('Radnicka 19, Novi Sad', 45.249101856630546, 19.848034);
insert into location(address, latitude, longitude) values('Zeleznicka 2, Novi Sad', 45.2493924092008, 19.840783700998372 );
insert into location(address, latitude, longitude) values('Cerska 34, Lacarak', 44.997668, 19.557723 );
insert into location(address, latitude, longitude) values ('Stevana Pesica, МЗ Коvilj, Коvilj', 45.2205569, 20.0257268);
insert into location(address, latitude, longitude) values ('Jevrejska 2, Novi Sad', 45.254090, 19.841760);
insert into location(address, latitude, longitude) values ('Vladike Platona 2, Novi Sad', 45.252250, 19.848140);
insert into location(address, latitude, longitude) values('Pionirska 4, Futog', 45.237780, 19.709290 );
insert into location(address, latitude, longitude) values ('Cara Dušana Silnog 64, Veternik', 45.2607023, 19.7611797);

insert into Route (mileage, departure_id, destination_id)
values (59.2, 1, 3);
insert into Route (mileage, departure_id, destination_id)
values (25.4, 4, 5);
insert into Route (mileage, departure_id, destination_id)
values (11.4, 7, 6);
insert into Route (mileage, departure_id, destination_id)
values (9.0, 8, 5);


insert into Vehicle
(is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
values
    (true, true, 'Audi A3', 4, 'KK077KK', 1, 1);

insert into Vehicle
(is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
values
    (true, true, 'Opel Astra G', 4, 'SM333RK', 2, 1);

insert into Vehicle
( is_baby_accessible, is_pet_accessible, model, number_of_seats, registration_plate, current_location_id, vehicle_type_id)
values
    (true, true, 'Mecedes Benz C class', 4, 'SM077CF', 3, 2);

insert into Drivers(id, vehicle_id) values(1, 1);
insert into Drivers(id, vehicle_id) values(2, 2);
-- insert into Drivers(id, vehicle_id) values(5, 3);
insert into Document(document_image, name, driver_id)
values ('dozvola', 'Saobracajna Dozvola', 1);
insert into Document(document_image, name, driver_id)
values ('dozvola', 'Vozacka Dozvola', 2);
insert into Document(document_image, name, driver_id)
values ('dozvola', 'Saobracajna Dozvola', 2);


insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_start, time_of_end, total_price, deduction_id, driver_id, vehicle_type_id)
values (48, true, True, false,'FINISHED',TO_DATE('26/7/2022 11:42', 'dd/MM/yyyy HH:mm'),
        TO_DATE('26/7/2022 12:34', 'dd/MM/yyyy HH:mm'), 4042, null, 1, 1);


insert into Drivers_Rides (driver_id, rides_id) values (1, 1);
insert into Passenger_Rides (passenger_id, rides_id) values (3, 1);
insert into Passenger_Rides (passenger_id, rides_id) values (4, 1);
insert into Ride_Passengers(ride_id, passengers_id) values (1, 3);
insert into Ride_Passengers(ride_id, passengers_id) values (1, 4);
insert into Ride_Locations (ride_id, locations_id) values (1, 1);
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 4, 'Vozilo je jako uredno', 5, 3, 1);
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo nije bas čisto', 3, 4, 1);
insert into Ride_Review (ride_id, review_id) values (1, 1);
insert into Ride_Review (ride_id, review_id) values (1, 2);

insert into Panic(time_of_press, reason, ride_id, user_id)
values(TO_DATE('15/11/2022 11:52', 'dd/MM/yyyy HH:mm'), 'Rider went of course', 1, 3);



insert into Ride ( estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                   status, time_of_end, time_of_start, total_price, deduction_id, driver_id, vehicle_type_id)
values (21, false , false, false,'FINISHED',TO_DATE('26/7/2022 18:35', 'dd/MM/yyyy HH:mm'),
        TO_DATE('26/7/2022 18:00', 'dd/MM/yyyy HH:mm'), 2045, null, 2, 3);

insert into Drivers_Rides (driver_id, rides_id) values (2, 2);
insert into Passenger_Rides (passenger_id, rides_id) values (4, 2);
insert into Passenger_Rides (passenger_id, rides_id) values (5, 2);
insert into Ride_Passengers(ride_id, passengers_id) values (2, 4);
insert into Ride_Passengers(ride_id, passengers_id) values (2, 5);
insert into Ride_Locations (ride_id, locations_id) values (2, 2);
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 4, 'Vozilo je jako uredno', 5, 5, 2);
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo nije bas čisto', 3, 4, 2);
insert into Ride_Review (ride_id, review_id) values (2, 3);
insert into Ride_Review (ride_id, review_id) values (2, 4);



insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_end, time_of_start, total_price, deduction_id, driver_id, vehicle_type_id)
values ( 16, true, false, false,'FINISHED',TO_DATE('20/9/2022 13:22', 'dd/MM/yyyy HH:mm'),
         TO_DATE('20/9/2022 13:00', 'dd/MM/yyyy HH:mm'), 1669, null, 2, 2);

insert into Drivers_Rides (driver_id, rides_id) values (2, 3);
insert into Passenger_Rides (passenger_id, rides_id) values (3, 3);
insert into Passenger_Rides (passenger_id, rides_id) values (5, 3);
insert into Ride_Passengers(ride_id, passengers_id) values (3, 3);
insert into Ride_Passengers(ride_id, passengers_id) values (3, 5);
insert into Ride_Locations (ride_id, locations_id) values (3, 3);
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo je jako uredno', 5, 3, 3);
insert into Review ( driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo nije bas čisto', 3, 5, 3);
insert into Ride_Review (ride_id, review_id) values (3, 5);
insert into Ride_Review (ride_id, review_id) values (3, 6);




insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_end, time_of_start, total_price, deduction_id, driver_id, vehicle_type_id)
values (9, true, false, false,'FINISHED',TO_DATE('11/11/2022 14:10', 'dd/MM/yyyy HH:mm'),
        TO_DATE('11/11/2022 14:00', 'dd/MM/yyyy HH:mm'), 903, null, 1, 1);

insert into Drivers_Rides (driver_id, rides_id) values (1, 4);
insert into Passenger_Rides (passenger_id, rides_id) values (4, 4);
insert into Ride_Passengers(ride_id, passengers_id) values (4, 4);
insert into Ride_Locations (ride_id, locations_id) values (4, 4); -- ok
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako neljubazan.', 2, 'Vozilo je jako prljavo', 3, 4, 4);
insert into Ride_Review (ride_id, review_id) values (4, 7);



insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_end, time_of_start, total_price, deduction_id, driver_id, vehicle_type_id)
values (9, false, false, true,'FINISHED',TO_DATE('15/11/2022 09:10', 'dd/MM/yyyy HH:mm'),
        TO_DATE('15/11/2022 08:50', 'dd/MM/yyyy HH:mm'), 903, null, 1, 1);

insert into Drivers_Rides (driver_id, rides_id) values (1, 5);
insert into Passenger_Rides (passenger_id, rides_id) values (4, 5);
insert into Ride_Passengers(ride_id, passengers_id) values (5, 4);
insert into Ride_Locations (ride_id, locations_id) values (5, 4); -- ok
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako neljubazan.', 2, 'Vozilo je jako prljavo', 3, 4, 5);
insert into Ride_Review (ride_id, review_id) values (5, 8);


insert into Panic(time_of_press, reason, ride_id, user_id)
values(TO_DATE('15/11/2022 09:10', 'dd/MM/yyyy HH:mm'), 'Rider went of course', 5, 3);



insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_end, time_of_start, total_price, deduction_id, driver_id, vehicle_type_id)
values (9, false, false, true,'FINISHED',TO_DATE('15/08/2022 09:10', 'dd/MM/yyyy HH:mm'),
        TO_DATE('15/08/2022 08:50', 'dd/MM/yyyy HH:mm'), 903, null, 1, 1);

insert into Drivers_Rides (driver_id, rides_id) values (1, 6);
insert into Passenger_Rides (passenger_id, rides_id) values (5, 6);
insert into Ride_Passengers(ride_id, passengers_id) values (6, 5);
insert into Ride_Locations (ride_id, locations_id) values (6, 4); -- ok
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo je jako prljavo', 3, 5, 6);
insert into Ride_Review (ride_id, review_id) values (6, 9);



insert into Ride (estimated_time, is_baby_accessible, is_panic_pressed, is_pet_accessible,
                  status, time_of_start, time_of_end, total_price, deduction_id, driver_id, vehicle_type_id)
values (9, false, false, true,'FINISHED',TO_DATE('11/11/2022 13:10', 'dd/MM/yyyy HH:mm'),
        TO_DATE('11/11/2022 13:50', 'dd/MM/yyyy HH:mm'), 903, null, 1, 1);

insert into Drivers_Rides (driver_id, rides_id) values (1, 7);
insert into Passenger_Rides (passenger_id, rides_id) values (5, 7);
insert into Ride_Passengers(ride_id, passengers_id) values (7, 5);
insert into Ride_Locations (ride_id, locations_id) values (7, 4); -- ok
insert into Review (driver_comment, driver_rating, vehicle_comment, vehicle_rating, passenger_id, ride_id)
values ('Vozač je bio jako ljubazan.', 5, 'Vozilo je solidno uredno', 4, 5, 7);
insert into Ride_Review (ride_id, review_id) values (7, 10);

