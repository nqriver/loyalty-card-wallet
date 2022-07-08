--liquibase formatted sql
--changeset nqriver:5

ALTER TABLE loyalty_cards
    ALTER COLUMN holder_email SET NOT NULL;


ALTER TABLE loyalty_cards ADD CONSTRAINT expiration_date_is_after_creation_date_check
    CHECK ( loyalty_cards.creation_time <= loyalty_cards.expiration_time )