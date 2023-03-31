

CREATE TABLE PRODUCT(
                        serialNumber uuid PRIMARY KEY,
                        deviceType VARCHAR(255) NOT NULL,
                        model VARCHAR(255) NOT NULL,
                        devicePurchaseDate date NOT NULL,
                        ownerID uuid NOT NULL,
                        warrantyDescription VARCHAR(255) NOT NULL,
                        warrantyExpirationDate date NOT NULL,
                        insurancePurchaseDate date,
                        insuranceExpirationDate date
);

CREATE TABLE PROFILE(
                        profileID uuid PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        surname VARCHAR(255) NOT NULL,
                        registrationDate date NOT NULL,
                        birthDate date NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phoneNumber VARCHAR(255) NOT NULL
);