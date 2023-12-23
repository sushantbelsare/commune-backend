--liquibase formatted sql

--changeset sushantbelsare:1
CREATE TABLE users ( username VARCHAR(255) PRIMARY KEY NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      first VARCHAR(255),
                      last VARCHAR(255),
                      art VARCHAR(255),
                      dob DATE,
                      email VARCHAR(255)
);