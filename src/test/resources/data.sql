insert into tag( name)
values ('FirstTag'),
       ('SecondTag'),
       ('ThirdTag'),
       ('FourthTag'),
       ('FifthTag');

insert into gift_certificate(name, description, price, create_date, last_update_date, duration)
VALUES ('Cerf1', 'desc1', 12.50, '2020-11-09 03:12:24', '2020-11-10 04:04:24', 1),
       ('Cerf2', 'desc2', 15.00, '2020-11-10 04:17:20', now(), 35),
       ('Cerf3', 'desc3', 4.00, '2020-11-10 03:10:12', '2020-11-10 06:04:56', 2);

INSERT INTO gift_certificate2tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2);