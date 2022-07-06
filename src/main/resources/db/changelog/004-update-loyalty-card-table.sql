--liquibase formatted sql
--changeset nqriver:4

ALTER TABLE loyalty_cards
    ADD holder_email EMAIL_DOMAIN UNIQUE,
    ADD creation_time TIMESTAMP NOT NULL DEFAULT current_timestamp,
    ADD expiration_time TIMESTAMP NOT NULL DEFAULT current_timestamp + interval '1 year';

