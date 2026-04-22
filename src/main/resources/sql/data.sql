INSERT INTO federation (id, name, created_at)
VALUES (1, 'Fédération Agricole de Madagascar', '2020-01-01');

INSERT INTO collectivity (id, number, name, location, agricultural_specialty, federation_id)
VALUES
    (gen_random_uuid(), 101, 'Collectif Riziculteurs Avaradrano', 'Antananarivo', 'Rice', 1),
    (gen_random_uuid(), 102, 'Union des Producteurs de Vanille', 'Sava', 'Vanilla', 1),
    (gen_random_uuid(), 103, 'Coopérative Maraîchère Itasy', 'Itasy', 'Vegetables', 1);

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    (gen_random_uuid(), 'Jean', 'Rakoto', '1990-05-12', 'MALE', 'Antananarivo', 'Farmer', '0340000001', 'jean.rakoto@mail.com'),
    (gen_random_uuid(), 'Marie', 'Rasoanaivo', '1985-03-20', 'FEMALE', 'Antsirabe', 'Trader', '0340000002', 'marie.raso@mail.com'),
    (gen_random_uuid(), 'Paul', 'Randria', '1995-07-10', 'MALE', 'Toamasina', 'Technician', '0340000003', 'paul.randria@mail.com'),
    (gen_random_uuid(), 'Lucie', 'Andrianina', '1998-11-02', 'FEMALE', 'Fianarantsoa', 'Student', '0340000004', 'lucie.andrianina@mail.com'),
    (gen_random_uuid(), 'Hery', 'Raveloson', '1980-01-15', 'MALE', 'Mahajanga', 'Accountant', '0340000005', 'hery.raveloson@mail.com');

INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at)
SELECT gen_random_uuid(), m.id, c.id, 'JUNIOR', CURRENT_DATE
FROM member m
         CROSS JOIN LATERAL (
    SELECT id FROM collectivity ORDER BY random() LIMIT 1
    ) c
LIMIT 5;

INSERT INTO membership_referee (id, membership_id, referee_member_id, referee_collectivity_id, relationship_nature)
SELECT
    gen_random_uuid(),
    cm.id,
    m2.id,
    cm.collectivity_id,
    'friend'
FROM collectivity_membership cm
         JOIN member m2 ON m2.id <> cm.member_id
LIMIT 10;