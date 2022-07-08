--liquibase formatted sql
--changeset nqriver:6

ALTER TABLE activities
    ADD CONSTRAINT positive_amount_of_points_check CHECK ( points > 0 );



ALTER TABLE activities
    ADD CONSTRAINT source_must_be_different_from_target_check
        CHECK ( activities.target_card_id <> activities.source_card_id )
