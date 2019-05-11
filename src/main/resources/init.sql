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

SET TIME ZONE 'Europe/Budapest';

DROP TABLE IF EXISTS favourites CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS main_pictures CASCADE;
DROP TABLE IF EXISTS pictures CASCADE;
DROP TABLE IF EXISTS real_estates CASCADE;
DROP TABLE IF EXISTS logger CASCADE;
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users(
    user_name varchar(40) PRIMARY KEY,
    email varchar(320) UNIQUE,
    password text NOT NULL,
    role_name varchar(10) NOT NULL DEFAULT 'renter',
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    unique_theme text,
    CONSTRAINT password_not_empty CHECK ( password <> '' )
);

CREATE TABLE admins(
    admin_id SERIAL PRIMARY KEY,
    user_name varchar(80) DEFAULT null,
    FOREIGN KEY (user_name) REFERENCES users(user_name) ON UPDATE CASCADE ON DELETE SET DEFAULT
);

CREATE TABLE logger(
    date TIMESTAMP WITH TIME ZONE NOT NULL,
    admin_id int,
    action text NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
);

CREATE TABLE real_estates(
    real_estate_id SERIAL PRIMARY KEY,
    user_name varchar(40),
    real_estate_name varchar(40) UNIQUE,
    country varchar(74) NOT NULL,
    city varchar(85) NOT NULL,
    address varchar(300) NOT NULL,
    bed_count int NOT NULL,
    price int NOT NULL,
    description text,
    extras text,
    upload_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    is_public boolean DEFAULT 'false',
    FOREIGN KEY (user_name) REFERENCES users(user_name),
    CONSTRAINT country_not_null CHECK ( country <> '' ),
    CONSTRAINT city_not_null CHECK ( city <> '' ),
    CONSTRAINT address_not_null CHECK ( address <> '' ),
    CONSTRAINT beds_not_null CHECK ( bed_count IS NOT NULL ),
    CONSTRAINT price_not_null CHECK ( price IS NOT NULL )
);

CREATE TABLE reservations(
    reservation_id SERIAL PRIMARY KEY,
    reservation_request_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    reservation_conformation_date TIMESTAMP WITH TIME ZONE DEFAULT NULL,
    real_estate_id int,
    tenant_name varchar(40),
    begins DATE NOT NULL,
    ends DATE NOT NULL,
    is_confirmed boolean DEFAULT false,
    FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id),
    FOREIGN KEY (tenant_name) REFERENCES users(user_name),
    CONSTRAINT begins_not_null CHECK ( begins IS NOT NULL ),
    CONSTRAINT ends_not_null CHECK ( ends IS NOT NULL )
);

/*isConfirmed boolean + before trigger for date chechking*/

CREATE TABLE messages(
    message_id SERIAL PRIMARY KEY,
    sender_name varchar(40), /*Sender's user name*/
    receiver_name varchar(40) DEFAULT 'system', /*system is a default superuser used for requests with no direct receivers of messages, e.g. role change requests*/
    real_estate int DEFAULT NULL, /* in case if the message is assigned to a user but not related to a real estate*/
    history int DEFAULT null,
    date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    title varchar(60) NOT NULL,
    message text NOT NULL,
    FOREIGN KEY (sender_name) REFERENCES users(user_name),
    FOREIGN KEY (history) REFERENCES messages(message_id),
    FOREIGN KEY (receiver_name) REFERENCES users(user_name),
    FOREIGN KEY (real_estate) REFERENCES real_estates(real_estate_id),
    CONSTRAINT title_not_null CHECK ( title <> '' )
);


CREATE TABLE favourites(
   user_name varchar(40),
   real_estate_id int,
   FOREIGN KEY (user_name) REFERENCES users(user_name),
   FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id)
);

CREATE TABLE reviews(
   id SERIAL PRIMARY KEY,
   date TIMESTAMP WITH TIME ZONE DEFAULT now(),
   real_estate_id int,
   user_name varchar(40),
   reservation_id int NOT NULL,
   reviewer_name varchar(40) NOT NULL,
   review text DEFAULT NULL,
   rating int,
   FOREIGN KEY (reviewer_name) REFERENCES users(user_name),
   FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id),
   FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
   FOREIGN KEY (user_name) REFERENCES users(user_name),
   CONSTRAINT rating_limit CHECK ( rating BETWEEN 1 AND 5),
   CONSTRAINT foreign_key_check CHECK ( real_estate_id IS NOT NULL OR user_name IS NOT NULL ),
   CONSTRAINT reviewer_not_null CHECK ( reviewer_name <> '' )
);

CREATE TABLE pictures(
    id SERIAL PRIMARY KEY,
    user_name varchar(40),
    real_estate int,
    picture bytea NOT NULL,
    FOREIGN KEY (user_name) REFERENCES users(user_name),
    FOREIGN KEY (real_estate) REFERENCES real_estates(real_estate_id),
    CONSTRAINT check_user_or_real_estate CHECK ( user_name IS NOT NULL OR real_estate IS NOT NULL ),
    CONSTRAINT check_pic_not_null CHECK ( picture <> '' )
);

CREATE TABLE main_pictures(
    user_name varchar(40) DEFAULT NULL,
    real_estate_id int DEFAULT NULL,
    picture_id int,
    FOREIGN KEY (user_name) REFERENCES users(user_name),
    FOREIGN KEY (real_estate_id) REFERENCES real_estates(real_estate_id),
    FOREIGN KEY (picture_id) REFERENCES pictures(id),
    CONSTRAINT check_nulls CHECK ( user_name IS NOT NULL OR real_estate_id IS NOT NULL )
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

CREATE OR REPLACE FUNCTION check_availability_for_reservations() RETURNS TRIGGER AS $b$
BEGIN
    IF EXISTS(SELECT * FROM reservations WHERE NEW.real_estate_id = real_estate_id AND (NEW.begins BETWEEN begins AND ends OR NEW.ends BETWEEN begins AND ends)) THEN
        RAISE EXCEPTION 'This date is not available';
    end if;
    RETURN NEW;
END; $b$
    LANGUAGE plpgsql;


CREATE TRIGGER check_availability
    BEFORE INSERT
    ON reservations
    FOR EACH ROW
EXECUTE PROCEDURE check_availability_for_reservations();

CREATE OR REPLACE FUNCTION add_user_to_admins() RETURNS TRIGGER AS $admin$
BEGIN
    IF NEW.role_name = 'admin' AND NOT EXISTS(SELECT * FROM admins WHERE user_name = NEW.user_name) THEN
        INSERT INTO admins (user_name) VALUES (NEW.user_name);
    end if;
    RETURN NEW;
end; $admin$
    LANGUAGE plpgsql;

CREATE TRIGGER add_user_to_admin
    AFTER UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE add_user_to_admins();

CREATE OR REPLACE FUNCTION remove_user_from_admins() RETURNS TRIGGER AS $admin$
BEGIN
    IF NEW.role_name != 'admin' AND EXISTS(SELECT * FROM admins WHERE user_name = NEW.user_name) THEN
        UPDATE admins SET user_name = NULL WHERE user_name = NEW.user_name;
    end if;
    RETURN NEW;
end; $admin$
    LANGUAGE plpgsql;

CREATE TRIGGER remove_user_from_admin
    AFTER UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE remove_user_from_admins();


CREATE OR REPLACE FUNCTION create_date_for_reservation_confirmation() RETURNS TRIGGER AS $conf$
BEGIN
    IF NEW.is_confirmed = 'true' AND OLD.reservation_conformation_date IS NULL THEN
        UPDATE reservations SET reservation_conformation_date = now() WHERE reservation_id = NEW.reservation_id;
    end if;
    RETURN NEW;
end; $conf$
    LANGUAGE plpgsql;

CREATE TRIGGER date_for_reservation_confirmation
    AFTER UPDATE
    ON reservations
    FOR EACH ROW
EXECUTE PROCEDURE create_date_for_reservation_confirmation();


CREATE OR REPLACE FUNCTION check_if_comment_valid() RETURNS TRIGGER AS $comment$
BEGIN
    IF EXISTS(SELECT * FROM reviews WHERE reservation_id = NEW.reservation_id AND reviewer_name = NEW.reviewer_name) THEN
        RAISE EXCEPTION 'You already reviewed this reservation before!';
    end if;
    RETURN NEW;
end; $comment$
    LANGUAGE plpgsql;

CREATE TRIGGER check_comment
    BEFORE INSERT
    ON reviews
    FOR EACH ROW
EXECUTE PROCEDURE check_if_comment_valid();

CREATE OR REPLACE FUNCTION check_if_comment_allowed() RETURNS TRIGGER AS $comment$
BEGIN
    IF NEW.reviewer_name NOT IN (SELECT tenant_name FROM reservations LEFT JOIN real_estates ON reservations.real_estate_id = real_estates.real_estate_id WHERE reservation_id = NEW.reservation_id) OR NEW.reviewer_name NOT IN (SELECT user_name FROM reservations LEFT JOIN real_estates ON reservations.real_estate_id = real_estates.real_estate_id WHERE reservation_id = NEW.reservation_id) THEN
        RAISE EXCEPTION 'You are not allowed to review this real estate or user!';
    end if;
    RETURN NEW;
end; $comment$
    LANGUAGE plpgsql;



INSERT INTO users VALUES ('system', 'basicmail@mail.hu', 'systemuser', 'admin', now(), 'theme');
INSERT INTO users VALUES ('test', 'test@test', 'password', 'renter', '2019/01/01 00:00', 'theme');
INSERT INTO users VALUES ('test1', 'test1@test', 'password', 'landlord', '2019/01/01 00:00', 'theme');
INSERT INTO users VALUES ('test2', 'test2@test', 'password', 'admin', '2019/01/01 00:00', 'theme');
INSERT INTO users VALUES ('test3', 'test3@test', 'password', 'admin', '2019/01/01 00:00', 'theme');

INSERT INTO admins (user_name) VALUES ('test2');
INSERT INTO admins (user_name) VALUES ('test3');


INSERT INTO real_estates(user_name, real_estate_name, country, city, address, bed_count, price, description, extras, upload_date) VALUES ('test1', 'fancyhouse', 'PonyLand', 'Butterfly', 'Csillámpóni str 12', 5, 12000, 'Lorem ipsum dolor sit amet', 'Roof, 4 walls', '2019/01/01 00:00');
INSERT INTO real_estates(user_name, real_estate_name, country, city, address, bed_count, price, description, extras, upload_date) VALUES ( 'test2', 'fancyhouse2', 'PonyLand', 'Butterfly', 'Csillámpóni str 12', 5, 12000, 'Lorem ipsum dolor sit amet', 'Roof, 4 walls', '2019/01/01 00:00');

INSERT INTO reservations(real_estate_id, tenant_name, begins, ends) VALUES ('1', 'test', '2019/01/01', '2019/01/06');

INSERT INTO messages(sender_name, receiver_name, date, title, message) VALUES ('test', 'test1', '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(sender_name, real_estate, date, title, message) VALUES ('test1', 1, '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(sender_name, receiver_name, real_estate, date, title, message) VALUES ('test2', 'test1', 1, '2019/01/06 02:33', 'Bla', 'Blablabla');
INSERT INTO messages(sender_name, date, title, message) VALUES ('test1', '2019/01/06 02:33', 'Bla', 'Blablabla');


END $$









