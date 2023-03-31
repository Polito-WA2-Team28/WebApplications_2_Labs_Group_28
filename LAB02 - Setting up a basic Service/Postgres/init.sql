CREATE TABLE PROFILE(
                        profile_id uuid PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        surname VARCHAR(255) NOT NULL,
                        registration_date date NOT NULL,
                        birth_date date NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE PRODUCT(
                        serial_number uuid PRIMARY KEY,
                        device_type VARCHAR(255) NOT NULL,
                        model VARCHAR(255) NOT NULL,
                        device_purchase_date date NOT NULL,
                        owner_id uuid NOT NULL,
                        warranty_description VARCHAR(255) NOT NULL,
                        warranty_expirationDate date NOT NULL,
                        insurance_purchaseDate date,
                        insurance_expirationDate date,
                        CONSTRAINT fk_customer
                        FOREIGN KEY(owner_id) 
                        REFERENCES PROFILE(profile_id)
      );