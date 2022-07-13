--liquibase formatted sql
--changeset nqriver:7

INSERT INTO loyalty_cards(id, holder_email, creation_time, expiration_time)
VALUES (1, 'firstemail@email.com', default, default),
       (2, 'secondemail@email.com', default, default),
       (3, 'thirdemail@email.com', default, default),
       (4, 'forth@email.com', default, default),
       (5, 'f5th@email.com', default, default),
       (6, 's6th@email.com', default, default);



INSERT INTO activities(timestamp, owner_card_id, source_card_id, target_card_id, points, incoming)
VALUES
--     initial deposit of 100 points to card of id 1
(current_timestamp - interval '1 year', 1, null, 1, 100, true),

--     initial deposit of 100 points to card of id 2
(current_timestamp - interval '1 year', 2, null, 2, 100, true),


--        transfer of 20 points from card of id 2 to card of id 1
(current_timestamp + interval '1 year', 1, 2, 1, 20, true),
(current_timestamp + interval '1 year', 2, 2, 1, 20, false),

--        transfer of 50 points from card of id 2 to card of id 1
(current_timestamp + interval '3 year', 1, 2, 1, 50, true),
(current_timestamp + interval '3 year', 2, 2, 1, 50, false),

--        transfer of 10 points from card of id 1 to card of id 2
(current_timestamp + interval '4 year', 2, 1, 2, 10, true),
(current_timestamp + interval '4 year', 1, 1, 2, 10, false),

--        transfer of 20 points from card of id 1 to card of id 5
(current_timestamp + interval '5 year', 5, 1, 5, 20, true),
(current_timestamp + interval '5 year', 1, 1, 5, 20, false);
