--liquibase formatted sql
--changeset nqriver:1

CREATE TABLE loyalty_cards (
    id SERIAL NOT NULL,
    CONSTRAINT pk_loyalty_cards PRIMARY KEY (id)
)