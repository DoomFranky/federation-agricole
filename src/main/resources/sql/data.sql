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

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    (gen_random_uuid(), 'Andry', 'Rakotomalala', '1992-04-10', 'MALE', 'Antananarivo', 'Farmer', '0340000101', 'andry.rakoto1@mail.com'),
    (gen_random_uuid(), 'Fara', 'Razanamihaja', '1988-09-22', 'FEMALE', 'Mahajanga', 'Trader', '0340000102', 'fara.razana@mail.com'),
    (gen_random_uuid(), 'Toky', 'Andrianarisoa', '1996-02-14', 'MALE', 'Toamasina', 'Technician', '0340000103', 'toky.andria@mail.com'),
    (gen_random_uuid(), 'Lova', 'Rabeharisoa', '1999-06-30', 'FEMALE', 'Antsirabe', 'Student', '0340000104', 'lova.rabe@mail.com'),
    (gen_random_uuid(), 'Mamy', 'Rakotondrabe', '1983-12-05', 'MALE', 'Fianarantsoa', 'Accountant', '0340000105', 'mamy.rakoto@mail.com'),

    (gen_random_uuid(), 'Hanta', 'Rasolofonirina', '1991-07-18', 'FEMALE', 'Toliara', 'Farmer', '0340000106', 'hanta.raso@mail.com'),
    (gen_random_uuid(), 'Rivo', 'Rakotovao', '1987-11-03', 'MALE', 'Antananarivo', 'Driver', '0340000107', 'rivo.rakoto@mail.com'),
    (gen_random_uuid(), 'Tahina', 'Andriatsitohaina', '1995-08-27', 'FEMALE', 'Antsirabe', 'Teacher', '0340000108', 'tahina.andria@mail.com'),
    (gen_random_uuid(), 'Solo', 'Raharison', '1982-03-12', 'MALE', 'Mahajanga', 'Fisherman', '0340000109', 'solo.rahari@mail.com'),
    (gen_random_uuid(), 'Voahangy', 'Rakotoarisoa', '1993-10-19', 'FEMALE', 'Toamasina', 'Nurse', '0340000110', 'voahangy.rakoto@mail.com'),

    (gen_random_uuid(), 'Faniry', 'Ravelomanantsoa', '1997-01-25', 'MALE', 'Antananarivo', 'Student', '0340000111', 'faniry.ravel@mail.com'),
    (gen_random_uuid(), 'Miora', 'Razanakoto', '1994-05-08', 'FEMALE', 'Fianarantsoa', 'Seller', '0340000112', 'miora.razana@mail.com'),
    (gen_random_uuid(), 'Tiana', 'Andriambololona', '1989-09-14', 'MALE', 'Antsirabe', 'Mechanic', '0340000113', 'tiana.andria@mail.com'),
    (gen_random_uuid(), 'Kanto', 'Rakotondrainibe', '1998-02-02', 'FEMALE', 'Antananarivo', 'Student', '0340000114', 'kanto.rakoto@mail.com'),
    (gen_random_uuid(), 'Njaka', 'Rabe', '1985-06-11', 'MALE', 'Mahajanga', 'Farmer', '0340000115', 'njaka.rabe@mail.com'),

    (gen_random_uuid(), 'Sarobidy', 'Ramanantsoa', '1990-12-21', 'FEMALE', 'Toamasina', 'Trader', '0340000116', 'sarobidy.rama@mail.com'),
    (gen_random_uuid(), 'Heriniaina', 'Rakotomalala', '1986-04-04', 'MALE', 'Antananarivo', 'Engineer', '0340000117', 'heriniaina@mail.com'),
    (gen_random_uuid(), 'Aina', 'Raveloson', '1993-07-29', 'FEMALE', 'Antsirabe', 'Teacher', '0340000118', 'aina.ravelo@mail.com'),
    (gen_random_uuid(), 'Feno', 'Randrianarisoa', '1991-11-17', 'MALE', 'Fianarantsoa', 'Driver', '0340000119', 'feno.randria@mail.com'),
    (gen_random_uuid(), 'Soa', 'Razanadrakoto', '1996-03-06', 'FEMALE', 'Toliara', 'Farmer', '0340000120', 'soa.razana@mail.com'),

    (gen_random_uuid(), 'Hery', 'Rakotondrazafy', '1984-08-13', 'MALE', 'Antananarivo', 'Accountant', '0340000121', 'hery.rakoto2@mail.com'),
    (gen_random_uuid(), 'Mialy', 'Andriamamonjy', '1999-01-09', 'FEMALE', 'Mahajanga', 'Student', '0340000122', 'mialy.andria@mail.com'),
    (gen_random_uuid(), 'Tokiniaina', 'Rabeharisoa', '1992-06-23', 'MALE', 'Antsirabe', 'Technician', '0340000123', 'tokiniaina@mail.com'),
    (gen_random_uuid(), 'Fitia', 'Rakotoniaina', '1995-10-01', 'FEMALE', 'Toamasina', 'Nurse', '0340000124', 'fitia.rakoto@mail.com'),
    (gen_random_uuid(), 'Ando', 'Rasoanaivo', '1988-02-28', 'MALE', 'Antananarivo', 'Trader', '0340000125', 'ando.raso@mail.com'),

    (gen_random_uuid(), 'Volana', 'Razanamihaja', '1997-05-15', 'FEMALE', 'Fianarantsoa', 'Teacher', '0340000126', 'volana@mail.com'),
    (gen_random_uuid(), 'Rija', 'Rakotobe', '1983-09-07', 'MALE', 'Mahajanga', 'Farmer', '0340000127', 'rija.rakoto@mail.com'),
    (gen_random_uuid(), 'Ony', 'Andrianasolo', '1994-12-11', 'FEMALE', 'Antsirabe', 'Seller', '0340000128', 'ony.andria@mail.com'),
    (gen_random_uuid(), 'Lanto', 'Ravelomanantsoa', '1991-03-03', 'MALE', 'Antananarivo', 'Mechanic', '0340000129', 'lanto@mail.com'),
    (gen_random_uuid(), 'Sitraka', 'Rakotondrabe', '1998-07-20', 'FEMALE', 'Toamasina', 'Student', '0340000130', 'sitraka@mail.com');

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