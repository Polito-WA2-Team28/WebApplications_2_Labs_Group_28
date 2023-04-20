# LAB02 - Team28


## Entrypoint details

- GET /API/products -- list all registered products in the DB
- GET /API/products/{productId} -- details of product {productId} or fail if it does not exist
- GET /API/profiles/{email} -- details of user profiles {email} or fail if it does not exist
- POST /API/profiles -- convert the request body into a ProfileDTO and store it into the DB, provided that the email address does not already exist
- PUT /API/profiles/{email} -- convert the request body into a ProfileDTO and replace the corresponding entry in the DB; fail if the email does not exist

## Tables

### PROFILE
| profile_id | name  | surname |  registration_date  |     birth_date      |        email        | phone_number |
| :--------: | :---: | :-----: | :-----------------: | :-----------------: | :-----------------: | :----------: |
|     1      | John  |   Doe   | 2022-03-15 12:00:00 | 1990-01-01 00:00:00 | johndoe@example.com |   555-1234   |
|     2      | Jane  |   Doe   | 2022-03-16 12:00:00 | 1992-05-10 00:00:00 | janedoe@example.com |   555-5678   |
 

### PRODUCT
| serial_number |  device_type  |        model        | device_purchase_date |           owner_id           | warranty_description | warranty_expiration_date | insurance_purchase_date | insurance_expiration_date |
| :-----------: | :-----------: | :-----------------: | :------------------: | :--------------------------: | :------------------: | :----------------------: | :---------------------: | :-----------------------: |
|    Laptop     |  Dell XPS 13  | 2022-03-15 12:00:00 |          1           | 3-year manufacturer warranty | 2025-03-15 12:00:00  |   2022-03-15 12:00:00    |   2023-03-15 12:00:00   |
|  Smartphone   | iPhone 14 Pro | 2022-03-16 12:00:00 |          2           | 1-year manufacturer warranty | 2023-03-16 12:00:00  |           NULL           |          NULL           |

## The web application

### Structure of the application

The web application developed in this laboratory has the following components:
- the **PostgreSQL database** containerized and deployed with a docker-compose configuration file. The PostgreSQL instance is listening on port 5432.
- the **SpringBoot backend** service running with Gradle and exposing the required APIs;
- the **React web client** the interacts with the backend services through the previously defined APIs.


### Run the application

To run the application:

1. Start the PostgreSQL instance (in *detached* mode) with:
   
    ```
    cd Postgres
    docker-compose up -d
    ```

2. Start the web application (both server and client) with:

    ```
    cd server
    gradle bootRun
    ```

3. Access the web application with any browser by reaching `localhost:8080`.