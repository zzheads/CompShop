INSERT INTO address (address_id, url, email, phone, first_name, last_name, country, city, zipcode, street) VALUES (1, 'amazon.com', 'support@amazon.com', '+7 777 7777777', 'Chris', 'Amazonoff', 'USA', 'Detroit', '40000', 'Belleware Street 12-45');
INSERT INTO address (address_id, url, email, phone, first_name, last_name, country, city, zipcode, street) VALUES (2, 'apple.com', 'support@apple.com', '+7 777 7777777', 'Steve', 'Jobs', 'USA', 'Cupertino', '40000', 'Los Angeles');

INSERT INTO supplier (supplier_id, name, address_id) VALUES (1, 'amazon.com', 1);
INSERT INTO supplier (supplier_id, name, address_id) VALUES (2, 'apple.com', 1);

INSERT INTO category (category_id, description, name) VALUES (1,'', 'PCHardware');
INSERT INTO category (category_id, description, name) VALUES (2,'', 'Wireless');