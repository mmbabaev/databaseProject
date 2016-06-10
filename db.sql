DROP TABLE IF EXISTS DRUG CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS DRUGSTORE CASCADE;
DROP TABLE IF EXISTS PRICE_CHANGE CASCADE;
DROP TABLE IF EXISTS DRUG_IN_STORE CASCADE;
DROP TABLE IF EXISTS INTERESTED_DRUG CASCADE;

CREATE TABLE IF NOT EXISTS DRUG
(
	drug_id SERIAL PRIMARY KEY,
	name VARCHAR (50)
);

insert into drug (drug_id, name) values (0, 'drug 0');
insert into drug (drug_id, name) values (1, 'drug 1');
insert into drug (drug_id, name) values (2, 'drug 2');
insert into drug (drug_id, name) values (3, 'drug 3');
insert into drug (drug_id, name) values (4, 'drug 4');
insert into drug (drug_id, name) values (5, 'drug 5');
insert into drug (drug_id, name) values (6, 'drug 6');
insert into drug (drug_id, name) values (7, 'drug 7');


CREATE TABLE IF NOT EXISTS USERS
(
	user_id SERIAL PRIMARY KEY,
	first_name VARCHAR (50),
	last_name VARCHAR (50),
	login VARCHAR (50),
	pass VARCHAR (50)
);



CREATE TABLE IF NOT EXISTS DRUGSTORE
(
	drugstore_id SERIAL PRIMARY KEY,
	name VARCHAR (100)
);

insert into drugstore (drugstore_id, name) VALUES (0, 'drugstore 0');
insert into drugstore (drugstore_id, name) VALUES (1, 'drugstore 1');
insert into drugstore (drugstore_id, name) VALUES (2, 'drugstore 2');
insert into drugstore (drugstore_id, name) VALUES (3, 'drugstore 3');
insert into drugstore (drugstore_id, name) VALUES (4, 'drugstore 4');



CREATE TABLE IF NOT EXISTS INTRESTED_DRUG
(
	intrested_drug_id SERIAL PRIMARY KEY,
	drug_id INTEGER references drug(drug_id),
	user_id INTEGER references users(user_id)
);




CREATE TABLE IF NOT EXISTS PRICE_CHANGE
(
	price_change_id SERIAL PRIMARY KEY,
	drug_id INTEGER references drug(drug_id),
	change_time TIMESTAMP,
	price REAL,
	drugstore_id INTEGER references drugstore(drugstore_id)
);

insert into price_change (drug_id, change_time, price, drugstore_id) 
	values (0, 10, 10, 0);
	
insert into price_change (drug_id, change_time, price, drugstore_id) 
	values (0, 10, 10, 1);
	
insert into price_change (drug_id, change_time, price, drugstore_id) 
	values (0, 10, 10, 2);
	
insert into price_change (drug_id, change_time, price, drugstore_id) 
	values (0, 10, 10, 3);

CREATE TABLE IF NOT EXISTS DRUG_IN_STORE
(
	drug_in_store_id SERIAL PRIMARY KEY,
	drug_id INTEGER references drug(drug_id),
	price REAL,
	drugstore_id INTEGER references drugstore(drugstore_id)
);

insert into drug_in_store (drug_id, price, drugstore_id) 
	Values (0, 10, 0);
	
insert into drug_in_store (drug_id, price, drugstore_id) 
	Values (0, 100, 1);
	
insert into drug_in_store (drug_id, price, drugstore_id) 
	Values (0, 1000, 2);

insert into drug_in_store (drug_id, price, drugstore_id) 
	Values (0, 10000, 3);