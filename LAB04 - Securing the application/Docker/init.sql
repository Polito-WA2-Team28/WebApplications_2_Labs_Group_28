-- CREATE TABLE PROFILE(
--                         profile_id SERIAL PRIMARY KEY,
--                         name VARCHAR(255) NOT NULL,
--                         surname VARCHAR(255) NOT NULL,
--                         registration_date timestamp NOT NULL,
--                         birth_date timestamp NOT NULL,
--                         email VARCHAR(255) NOT NULL,
--                         phone_number VARCHAR(255) NOT NULL
-- );

-- CREATE TABLE PRODUCT(
--                         serial_number SERIAL PRIMARY KEY,
--                         device_type VARCHAR(255) NOT NULL,
--                         model VARCHAR(255) NOT NULL,
--                         device_purchase_date timestamp NOT NULL,
--                         owner_id SERIAL NOT NULL,
--                         warranty_description VARCHAR(255) NOT NULL,
--                         warranty_expiration_date timestamp NOT NULL,
--                         insurance_purchase_date timestamp,
--                         insurance_expiration_date timestamp,
--                         CONSTRAINT fk_customer
--                         FOREIGN KEY(owner_id) 
--                         REFERENCES PROFILE(profile_id)
--       );



-- INSERT INTO PROFILE (name, surname, registration_date, birth_date, email, phone_number)
-- VALUES 
--   ('John', 'Doe', '2022-03-15 12:00:00', '1990-01-01 00:00:00', 'johndoe@example.com', '555-1234'),
--   ('Jane', 'Doe', '2022-03-16 12:00:00', '1992-05-10 00:00:00', 'janedoe@example.com', '555-5678'),
--   ('Bob', 'Smith', '2022-03-17 12:00:00', '1985-08-23 00:00:00', 'bobsmith@example.com', '555-9101'),
--   ('Alice', 'Johnson', '2022-03-18 12:00:00', '1998-11-17 00:00:00', 'alicejohnson@example.com', '555-1213');

-- -- Insert 10 products
-- INSERT INTO PRODUCT (device_type, model, device_purchase_date, owner_id, warranty_description, warranty_expiration_date, insurance_purchase_date, insurance_expiration_date)
-- VALUES 
--   ('Laptop', 'Dell XPS 13', '2022-03-15 12:00:00', 1, '3-year manufacturer warranty', '2025-03-15 12:00:00', '2022-03-15 12:00:00', '2023-03-15 12:00:00'),
--   ('Smartphone', 'iPhone 14 Pro', '2022-03-16 12:00:00', 2, '1-year manufacturer warranty', '2023-03-16 12:00:00', NULL, NULL),
--   ('Tablet', 'Samsung Galaxy Tab S7', '2022-03-17 12:00:00', 1, '2-year manufacturer warranty', '2024-03-17 12:00:00', '2022-03-17 12:00:00', '2023-03-17 12:00:00'),
--   ('Desktop', 'HP Envy Desktop', '2022-03-18 12:00:00', 3, '2-year manufacturer warranty', '2024-03-18 12:00:00', NULL, NULL),
--   ('Laptop', 'Lenovo ThinkPad X1 Carbon', '2022-03-19 12:00:00', 4, '3-year manufacturer warranty', '2025-03-19 12:00:00', '2022-03-19 12:00:00', '2023-03-19 12:00:00'),
--   ('Smartphone', 'Samsung Galaxy S22', '2022-03-20 12:00:00', 1, '1-year manufacturer warranty', '2023-03-20 12:00:00', NULL, NULL),
--   ('Tablet', 'iPad Pro 12.9', '2022-03-21 12:00:00', 2, '2-year manufacturer warranty', '2024-03-21 12:00:00', '2022-03-21 12:00:00', '2023-03-21 12:00:00');


