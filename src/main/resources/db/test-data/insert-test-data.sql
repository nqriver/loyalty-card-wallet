--liquibase formatted sql
--changeset nqriver:7

INSERT INTO loyalty_cards(id, holder_email, creation_time, expiration_time)
VALUES (1, 'firstemail@email.com', default, default),
       (2, 'secondemail@email.com', default, default),
       (3, 'thirdemail@email.com', default, default),
       (4, 'forth@email.com', default, default),
       (5, 'f5th@email.com', default, default),
       (6, 's6th@email.com', default, default);




INSERT INTO activities(id, timestamp, owner_card_id, source_card_id, target_card_id, points, incoming)
VALUES (1, current_timestamp - interval '1 year', 1, 2, 1, 20, true),
       (2, current_timestamp + interval '3 year', 1, 2, 1, 50, true),
       (3, current_timestamp + interval '4 year', 2, 1, 2, 10, true),
       (4, current_timestamp + interval '5 year', 1, 5, 1, 20, true);