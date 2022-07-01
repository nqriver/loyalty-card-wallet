--liquibase formatted sql
--changeset nqriver:2

CREATE TABLE activities
(
    id             SERIAL    NOT NULL,
    timestamp      TIMESTAMP NOT NULL,
    owner_card_id  BIGINT    NOT NULL,
    source_card_id BIGINT,
    target_card_id BIGINT,
    points         BIGINT    NOT NULL,
    incoming       BOOLEAN   NOT NULL,
    CONSTRAINT pk_activites PRIMARY KEY (id)
);


ALTER TABLE activities
    ADD CONSTRAINT fk_owner_card_id FOREIGN KEY (owner_card_id) REFERENCES loyalty_cards (id);

ALTER TABLE activities
    ADD CONSTRAINT fk_source_card_id FOREIGN KEY (source_card_id) REFERENCES loyalty_cards (id);

ALTER TABLE activities
    ADD CONSTRAINT fk_target_card_id FOREIGN KEY (target_card_id) REFERENCES loyalty_cards (id);

