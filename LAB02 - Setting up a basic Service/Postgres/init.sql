CREATE TABLE PROFILE(
                        profile_id uuid PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        surname VARCHAR(255) NOT NULL,
                        registration_date timestamp NOT NULL,
                        birth_date timestamp NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE PRODUCT(
                        serial_number uuid PRIMARY KEY,
                        device_type VARCHAR(255) NOT NULL,
                        model VARCHAR(255) NOT NULL,
                        device_purchase_date timestamp NOT NULL,
                        owner_id uuid NOT NULL,
                        warranty_description VARCHAR(255) NOT NULL,
                        warranty_expiration_date timestamp NOT NULL,
                        insurance_purchase_date timestamp,
                        insurance_expiration_date timestamp,
                        CONSTRAINT fk_customer
                        FOREIGN KEY(owner_id) 
                        REFERENCES PROFILE(profile_id)
      );