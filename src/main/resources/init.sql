SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET default_tablespace = '';

SET default_with_oids = false;

SET TIME ZONE 'GMT+2';

DROP TABLE IF EXISTS favourites;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS real_estates;
DROP TABLE IF EXISTS logger;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS users;








CREATE TABLE users(
    user_name varchar(40) PRIMARY KEY,
    email varchar(320) UNIQUE,
    password text NOT NULL,
    role_name varchar(10) NOT NULL DEFAULT 'renter',
    registration_date TIMESTAMP,
    profile_picture text, /*text for testing purposes, will be corrected to bytea to store pictures*/
    unique_theme text
);

CREATE TABLE admins(
    admin_id varchar(6) PRIMARY KEY,
    user_name varchar(40),
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE logger(
    date TIMESTAMP NOT NULL,
    admin_id varchar(6),
    action text NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
);

CREATE TABLE real_estates(
    real_estate_id varchar(6) PRIMARY KEY,
    user_name varchar(40),
    real_estate_name varchar(40) UNIQUE,
    country varchar(74) NOT NULL,
    city varchar(85) NOT NULL,
    address varchar(300) NOT NULL,
    bed_count int NOT NULL,
    price int NOT NULL,
    description text,
    extras text,
    picture text, /*text for testing purposes, will be corrected to bytea to store pictures*/
    upload_date DATE NOT NULL,
    is_public boolean DEFAULT 'false',
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE reservations(
    reservation_id varchar(6) PRIMARY KEY,
    real_estate_id varchar(6),
    user_name varchar(40),
    starting_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id),
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE messages(
    message_id varchar(6) PRIMARY KEY,
    user_name varchar(40), /*Sender's user name*/
    date TIMESTAMP NOT NULL,
    title varchar(60) NOT NULL,
    message text NOT NULL,
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE messages_receivers(
    message_id varchar(6),
    user_name varchar(40),
    FOREIGN KEY (message_id) REFERENCES messages(message_id),
    FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE messages_realestates(
    message_id varchar(6),
    real_estate_id varchar(6),
    FOREIGN KEY (message_id) REFERENCES messages(message_id),
    FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id)
);

CREATE TABLE favourites(
   user_name varchar(40),
   real_estate_id varchar(6),
   FOREIGN KEY (user_name) REFERENCES users(user_name),
   FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id)
);


INSERT INTO users VALUES ('test', 'test@test', 'password', 'renter', '2019/01/01 00:00', 'pic', 'theme');
INSERT INTO users VALUES ('test1', 'test1@test', 'password', 'landlord', '2019/01/01 00:00', 'pic', 'theme');
INSERT INTO users VALUES ('test2', 'test2@test', 'password', 'admin', '2019/01/01 00:00', 'pic', 'theme');

INSERT INTO admins VALUES ('123abc', 'test2');

INSERT INTO logger VALUES ('2019/01/01 00:00', '123abc', 'Test3 added realestate123');
INSERT INTO logger VALUES ('2019/01/01 00:00', '123abc', 'Test3 added test1 as landlord');

INSERT INTO real_estates VALUES ('234abc', 'test1', 'fancyhouse', 'PonyLand', 'Butterfly', 'Csillámpóni str 12', 5, 12000, 'Lorem ipsum dolor sit amet', 'Roof, 4 walls', 'fancypic', '2019/01/01 00:00');

INSERT INTO reservations VALUES ('345abd', '234abc', 'test', '2019/01/01', '2019/01/06');

INSERT INTO messages VALUES ('456abd', 'test', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages VALUES ('456abe', 'test1', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages VALUES ('456abf', 'test2', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages VALUES ('456abg', 'test1', '2019/01/06 02:33', 'Bla', 'Blablabla');

INSERT INTO messages_realestates VALUES ('456abd', '234abc');
INSERT INTO messages_realestates VALUES ('456abf', '234abc');

INSERT INTO messages_receivers VALUES ('456abe', 'test');
INSERT INTO messages_receivers VALUES ('456abg', 'test2');











