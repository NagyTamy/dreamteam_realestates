DO $$ BEGIN

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

/*For testing purposes, session.osuser will pass the logged in user from the current session to the sql script from java dao*/
SET session.osuser to 'test2';

SET default_tablespace = '';

SET default_with_oids = false;

SET TIME ZONE 'GMT+2';

DROP TABLE IF EXISTS favourites CASCADE;
DROP TABLE IF EXISTS messages_receivers CASCADE;
DROP TABLE IF EXISTS messages_realestates CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS real_estates CASCADE;
DROP TABLE IF EXISTS logger CASCADE;
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS users CASCADE;


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
    user_name varchar(80) DEFAULT null,
    FOREIGN KEY (user_name) REFERENCES users(user_name) ON UPDATE CASCADE ON DELETE SET DEFAULT
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
    reservation_conformation_date TIMESTAMP,
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
    history varchar(6) DEFAULT null,
    date TIMESTAMP NOT NULL,
    title varchar(60) NOT NULL,
    message text NOT NULL,
    FOREIGN KEY (user_name) REFERENCES users(user_name),
    FOREIGN KEY (history) REFERENCES messages(message_id)
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



CREATE OR REPLACE FUNCTION logger_for_users_table()
RETURNS trigger AS $A$
    DECLARE
        this_name text;
    BEGIN
        this_name := NEW.user_name;
        IF (SELECT user_name FROM users AS current_admin WHERE user_name = (SELECT current_setting('session.osuser'))) != NEW.user_name THEN
            IF (TG_OP = 'INSERT') THEN
                INSERT INTO logger(date, admin_id, action)
                VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), 'New user '|| this_name ||' with role '|| NEW.role_name || ' has been added to the system.');
            ELSEIF (TG_OP = 'UPDATE') THEN
                INSERT INTO logger(date, admin_id, action)
                VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), this_name ||' user''s role now: '|| NEW.role_name);
            END IF;
        END IF;
        RETURN NEW;
    END; $A$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION logger_for_users_table_delete()
    RETURNS trigger AS $A$
DECLARE
    this_name text;
BEGIN
    this_name := OLD.user_name;
    IF (SELECT user_name FROM users AS current_admin WHERE user_name = (SELECT current_setting('session.osuser'))) != OLD.user_name THEN
        INSERT INTO logger(date, admin_id, action)
        VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), this_name ||' user''s data deleted from users.');
    END IF;
    RETURN NEW;
END; $A$
    LANGUAGE plpgsql;

CREATE TRIGGER logger_for_users_table_insert
    AFTER INSERT
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_users_table();

CREATE TRIGGER logger_for_users_table_update
    AFTER UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_users_table();

CREATE TRIGGER logger_for_users_table_delete
    AFTER DELETE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_users_table_delete();


CREATE OR REPLACE FUNCTION logger_for_real_estate_table()
    RETURNS trigger AS $A$
DECLARE
    this_house text;
BEGIN
    this_house := NEW.real_estate_name;
    IF (SELECT user_name FROM real_estates AS current_admin WHERE user_name = (SELECT current_setting('session.osuser'))) != NEW.user_name THEN
        IF (TG_OP = 'INSERT') THEN
            INSERT INTO logger(date, admin_id, action)
            VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), this_house ||' on the id: ' || NEW.real_estate_id || ' has been added with a public status: '|| NEW.is_public);
        ELSEIF (TG_OP='UPDATE') THEN
            INSERT INTO logger(date, admin_id, action)
            VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), this_house ||' real estate''s public status now: ' || NEW.is_public);
        END IF;
    END IF;
    RETURN NEW;
END; $A$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION logger_for_real_estate_table_delete()
    RETURNS trigger AS $A$
DECLARE
    this_house text;
BEGIN
    this_house := OLD.real_estate_name;
    IF (SELECT user_name FROM real_estates AS current_admin WHERE user_name = (SELECT current_setting('session.osuser'))) != OLD.user_name THEN
        INSERT INTO logger(date, admin_id, action)
        VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), this_house ||' real estate''s data deleted.');
    END IF;
    RETURN NEW;
END; $A$
    LANGUAGE plpgsql;


CREATE TRIGGER logger_for_real_estate_table_insert
    AFTER INSERT
    ON real_estates
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_real_estate_table();

CREATE TRIGGER logger_for_real_estate_table_update
    AFTER UPDATE
    ON real_estates
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_real_estate_table();

CREATE TRIGGER logger_for_real_estate_table_delete
    AFTER DELETE
    ON real_estates
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_real_estate_table_delete();

/*Checks history of outgoing admin message, if previous message is user request (a.k.a. it doesnt have a receiver in users_messages table it logs the */

CREATE OR REPLACE FUNCTION logger_for_user_requests()
    RETURNS trigger AS $A$
BEGIN
    IF NEW.history IS NOT NULL AND NEW.history NOT IN (SELECT messages.message_id FROM messages RIGHT JOIN messages_receivers ON messages.message_id = messages_receivers.message_id) THEN
        INSERT INTO logger(date, admin_id, action)
        VALUES(now(),(SELECT admin_id FROM admins WHERE user_name = (SELECT current_setting('session.osuser'))), (SELECT current_setting('session.osuser'))|| ' answered ' ||(SELECT  user_name FROM messages WHERE message_id = NEW.history)|| '''s message nr.: ' || NEW.history );
    END IF;
    RETURN NEW;
END; $A$
    LANGUAGE plpgsql;


CREATE TRIGGER logger_for_user_request
    AFTER INSERT
    ON messages
    FOR EACH ROW
EXECUTE PROCEDURE logger_for_user_requests();


INSERT INTO users VALUES ('test', 'test@test', 'password', 'renter', '2019/01/01 00:00', 'pic', 'theme');
INSERT INTO users VALUES ('test1', 'test1@test', 'password', 'landlord', '2019/01/01 00:00', 'pic', 'theme');
INSERT INTO users VALUES ('test2', 'test2@test', 'password', 'admin', '2019/01/01 00:00', 'pic', 'theme');
INSERT INTO users VALUES ('test3', 'test3@test', 'password', 'admin', '2019/01/01 00:00', 'pic', 'theme');

INSERT INTO admins VALUES ('123abc', 'test2');
INSERT INTO admins VALUES ('123abt', 'test3');


INSERT INTO real_estates VALUES ('234abc', 'test1', 'fancyhouse', 'PonyLand', 'Butterfly', 'Csillámpóni str 12', 5, 12000, 'Lorem ipsum dolor sit amet', 'Roof, 4 walls', 'fancypic', '2019/01/01 00:00');
INSERT INTO real_estates VALUES ('2s4abc', 'test2', 'fancyhouse2', 'PonyLand', 'Butterfly', 'Csillámpóni str 12', 5, 12000, 'Lorem ipsum dolor sit amet', 'Roof, 4 walls', 'fancypic', '2019/01/01 00:00');

INSERT INTO reservations VALUES (now(),'345abd', '234abc', 'test', '2019/01/01', '2019/01/06');

INSERT INTO messages(message_id, user_name, date, title, message) VALUES ('456abd', 'test', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(message_id, user_name, date, title, message) VALUES ('456abe', 'test1', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(message_id, user_name, date, title, message) VALUES ('456abf', 'test2', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(message_id, user_name, date, title, message) VALUES ('456abg', 'test1', '2019/01/06 02:33', 'Bla', 'Blablabla');

INSERT INTO messages_realestates VALUES ('456abd', '234abc');
INSERT INTO messages_realestates VALUES ('456abf', '234abc');

INSERT INTO messages_receivers VALUES ('456abe', 'test');

END $$









