-- ============================================================================
-- FEDERATION
-- ============================================================================

INSERT INTO federation (id, name)
VALUES
    (1, 'Fédération Agricole');

-- ============================================================================
-- COLLECTIVITIES
-- ============================================================================

INSERT INTO collectivity (
    id,
    number,
    name,
    location,
    agricultural_specialty,
    federation_id
)
VALUES
    ('col-1', 1, 'Mpanorina', 'Ambatondrazaka', 'Riziculture', 1),
    ('col-2', 2, 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture', 1),
    ('col-3', 3, 'Tantely mamy', 'Brickaville', 'Apiculture', 1);

-- ============================================================================
-- MEMBERS
-- ============================================================================

INSERT INTO member (
    id,
    first_name,
    last_name,
    birth_date,
    gender,
    address,
    profession,
    phone_number,
    email
)
VALUES
    (
        'C1-M1',
        'Prénom membre1',
        'Nom membre1',
        '1980-02-01',
        'MALE',
        'LotIIV MAmbato.',
        'Riziculteur',
        '0341234567',
        'member.1@fed-agri.mg'
    ),
    (
        'C1-M2',
        'Prénom membre2',
        'Nom membre2',
        '1982-03-05',
        'MALE',
        'LotIIF Ambato.',
        'Agriculteur',
        '0321234567',
        'member.2@fed-agri.mg'
    ),
    (
        'C1-M3',
        'Prénom membre3',
        'Nom membre3',
        '1992-03-10',
        'MALE',
        'LotIIJ Ambato.',
        'Collecteur',
        '0331234567',
        'member.3@fed-agrimg'
    ),
    (
        'C1-M4',
        'Prénom membre4',
        'Nom membre4',
        '1988-05-22',
        'FEMALE',
        'LotAK50 Ambato.',
        'Distributeur',
        '0381234567',
        'member.4@fed-agri.mg'
    ),
    (
        'C1-M5',
        'Prénom membre5',
        'Nom membre5',
        '1999-08-21',
        'MALE',
        'LotUV80 Ambato.',
        'Riziculteur',
        '0373434567',
        'member.5@fed-agri.mg'
    ),
    (
        'C1-M6',
        'Prénom membre6',
        'Nom membre6',
        '1998-08-22',
        'FEMALE',
        'LotUV6 Ambato.',
        'Riziculteur',
        '0372234567',
        'member.6@fed-agri.mg'
    ),
    (
        'C1-M7',
        'Prénom membre7',
        'Nom membre7',
        '1998-01-31',
        'MALE',
        'LotUV7 Ambato.',
        'Riziculteur',
        '0374234567',
        'member.7@fed-agri.mg'
    ),
    (
        'C1-M8',
        'Prénom membre6',
        'Nom membre8',
        '1975-08-20',
        'MALE',
        'LotUV8 Ambato.',
        'Riziculteur',
        '0370234567',
        'member.8@fed-agri.mg'
    ),
    (
        'C3-M1',
        'Prénom membre9',
        'Nom membre9',
        '1988-01-02',
        'MALE',
        'Lot33J Antsirabe',
        'Apiculteur',
        '034034567',
        'member.9@fed-agri.mg'
    ),
    (
        'C3-M2',
        'Prénom membre10',
        'Nom membre10',
        '1982-03-05',
        'MALE',
        'Lot2J Antsirabe',
        'Agriculteur',
        '0338634567',
        'member.10@fed-agri.mg'
    ),
    (
        'C3-M3',
        'Prénom membre11',
        'Nom membre11',
        '1992-03-12',
        'MALE',
        'Lot8KM Antsirabe',
        'Collecteur',
        '0338234567',
        'member.11@fed-agrimg'
    ),
    (
        'C3-M4',
        'Prénom membre12',
        'Nom membre12',
        '1988-05-10',
        'FEMALE',
        'LotAK50 Antsirabe',
        'Distributeur',
        '0382334567',
        'member.12@fed-agri.mg'
    ),
    (
        'C3-M5',
        'Prénom membre13',
        'Nom membre13',
        '1999-08-11',
        'MALE',
        'LotUV80 Antsirabe.',
        'Apiculteur',
        '0373365567',
        'member.13@fed-agri.mg'
    ),
    (
        'C3-M6',
        'Prénom membre14',
        'Nom membre14',
        '1998-08-09',
        'FEMALE',
        'LotUV6 Antsirabe.',
        'Apiculteur',
        '0378234567',
        'member.14@fed-agri.mg'
    ),
    (
        'C3-M7',
        'Prénom membre15',
        'Nom membre15',
        '1998-01-13',
        'MALE',
        'LotUV7 Antsirabe',
        'Apiculteur',
        '0374914567',
        'member.15@fed-agri.mg'
    ),
    (
        'C3-M8',
        'Prénom membre16',
        'Nom membre16',
        '1975-08-02',
        'MALE',
        'LotUV8 Antsirabe',
        'Apiculteur',
        '0370634567',
        'member.16@fed-agri.mg'
    );

-- ============================================================================
-- COLLECTIVITY MEMBERSHIPS
-- ============================================================================

INSERT INTO collectivity_membership (
    id,
    member_id,
    collectivity_id,
    occupation
)
VALUES

-- COLLECTIVITY 1

('CM-C1-M1', 'C1-M1', 'col-1', 'PRESIDENT'),
('CM-C1-M2', 'C1-M2', 'col-1', 'VICE_PRESIDENT'),
('CM-C1-M3', 'C1-M3', 'col-1', 'SECRETARY'),
('CM-C1-M4', 'C1-M4', 'col-1', 'TREASURER'),
('CM-C1-M5', 'C1-M5', 'col-1', 'SENIOR'),
('CM-C1-M6', 'C1-M6', 'col-1', 'SENIOR'),
('CM-C1-M7', 'C1-M7', 'col-1', 'SENIOR'),
('CM-C1-M8', 'C1-M8', 'col-1', 'SENIOR'),

-- COLLECTIVITY 2

('CM-C2-M1', 'C1-M1', 'col-2', 'SENIOR'),
('CM-C2-M2', 'C1-M2', 'col-2', 'SENIOR'),
('CM-C2-M3', 'C1-M3', 'col-2', 'SENIOR'),
('CM-C2-M4', 'C1-M4', 'col-2', 'SENIOR'),
('CM-C2-M5', 'C1-M5', 'col-2', 'PRESIDENT'),
('CM-C2-M6', 'C1-M6', 'col-2', 'VICE_PRESIDENT'),
('CM-C2-M7', 'C1-M7', 'col-2', 'SECRETARY'),
('CM-C2-M8', 'C1-M8', 'col-2', 'TREASURER'),

-- COLLECTIVITY 3

('CM-C3-M1', 'C3-M1', 'col-3', 'PRESIDENT'),
('CM-C3-M2', 'C3-M2', 'col-3', 'VICE_PRESIDENT'),
('CM-C3-M3', 'C3-M3', 'col-3', 'SECRETARY'),
('CM-C3-M4', 'C3-M4', 'col-3', 'TREASURER'),
('CM-C3-M5', 'C3-M5', 'col-3', 'SENIOR'),
('CM-C3-M6', 'C3-M6', 'col-3', 'SENIOR'),
('CM-C3-M7', 'C3-M7', 'col-3', 'SENIOR'),
('CM-C3-M8', 'C3-M8', 'col-3', 'SENIOR');

-- ============================================================================
-- MEMBERSHIP REFEREES
-- ============================================================================

INSERT INTO membership_referee (
    membership_id,
    referee_member_id,
    referee_collectivity_id,
    relationship_nature
)
VALUES

    ('CM-C1-M3', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M3', 'C1-M2', 'col-1', NULL),

    ('CM-C1-M4', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M4', 'C1-M2', 'col-1', NULL),

    ('CM-C1-M5', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M5', 'C1-M2', 'col-1', NULL),

    ('CM-C1-M6', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M6', 'C1-M2', 'col-1', NULL),

    ('CM-C1-M7', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M7', 'C1-M2', 'col-1', NULL),

    ('CM-C1-M8', 'C1-M6', 'col-1', NULL),
    ('CM-C1-M8', 'C1-M7', 'col-1', NULL),

    ('CM-C2-M3', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M3', 'C1-M2', 'col-1', NULL),

    ('CM-C2-M4', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M4', 'C1-M2', 'col-1', NULL),

    ('CM-C2-M5', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M5', 'C1-M2', 'col-1', NULL),

    ('CM-C2-M6', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M6', 'C1-M2', 'col-1', NULL),

    ('CM-C2-M7', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M7', 'C1-M2', 'col-1', NULL),

    ('CM-C2-M8', 'C1-M6', 'col-1', NULL),
    ('CM-C2-M8', 'C1-M7', 'col-1', NULL),

    ('CM-C3-M1', 'C1-M1', 'col-1', NULL),
    ('CM-C3-M1', 'C1-M2', 'col-1', NULL),

    ('CM-C3-M2', 'C1-M1', 'col-1', NULL),
    ('CM-C3-M2', 'C1-M2', 'col-1', NULL),

    ('CM-C3-M3', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M3', 'C3-M2', 'col-3', NULL),

    ('CM-C3-M4', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M4', 'C3-M2', 'col-3', NULL),

    ('CM-C3-M5', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M5', 'C3-M2', 'col-3', NULL),

    ('CM-C3-M6', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M6', 'C3-M2', 'col-3', NULL),

    ('CM-C3-M7', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M7', 'C3-M2', 'col-3', NULL),

    ('CM-C3-M8', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M8', 'C3-M2', 'col-3', NULL);

-- ============================================================================
-- DUES RULES
-- ============================================================================

INSERT INTO dues_rule (
    id,
    collectivity_id,
    frequency,
    amount_mga,
    label,
    effective_from
)
VALUES
    (
        'cot-1',
        'col-1',
        'ANNUALLY',
        100000,
        'Cotisation annuelle',
        '2026-01-01'
    ),
    (
        'cot-2',
        'col-2',
        'ANNUALLY',
        100000,
        'Cotisation annuelle',
        '2026-01-01'
    ),
    (
        'cot-3',
        'col-3',
        'ANNUALLY',
        50000,
        'Cotisation annuelle',
        '2026-01-01'
    );

-- ============================================================================
-- TREASURY ACCOUNTS
-- ============================================================================

INSERT INTO treasury_account (
    id,
    collectivity_id,
    account_type,
    balance_mga
)
VALUES
    ('C1-A-CASH', 'col-1', 'CASH', 0),
    ('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0),

    ('C2-A-CASH', 'col-2', 'CASH', 0),
    ('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0),

    ('C3-A-CASH', 'col-3', 'CASH', 0);

-- ============================================================================
-- MOBILE MONEY ACCOUNTS
-- ============================================================================

INSERT INTO mobile_money_account (
    treasury_account_id,
    account_holder_name,
    provider,
    phone_number
)
VALUES
    (
        'C1-A-MOBILE-1',
        'Mpanorina',
        'ORANGE_MONEY',
        '0370489612'
    ),
    (
        'C2-A-MOBILE-1',
        'Dobo voalohany',
        'ORANGE_MONEY',
        '0320489612'
    );

-- ============================================================================
-- PAYMENT RECEIPTS
-- ============================================================================

INSERT INTO payment_receipt (
    collectivity_membership_id,
    dues_rule_id,
    amount_mga,
    payment_method,
    collected_at
)
VALUES

-- COLLECTIVITY 1

('CM-C1-M1', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M2', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M3', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M4', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M5', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M6', 'cot-1', 100000, 'CASH', '2026-01-01'),
('CM-C1-M7', 'cot-1', 60000, 'CASH', '2026-01-01'),
('CM-C1-M8', 'cot-1', 90000, 'CASH', '2026-01-01'),

-- COLLECTIVITY 2

('CM-C2-M1', 'cot-2', 60000, 'CASH', '2026-01-01'),
('CM-C2-M2', 'cot-2', 90000, 'CASH', '2026-01-01'),
('CM-C2-M3', 'cot-2', 100000, 'CASH', '2026-01-01'),
('CM-C2-M4', 'cot-2', 100000, 'CASH', '2026-01-01'),
('CM-C2-M5', 'cot-2', 100000, 'CASH', '2026-01-01'),
('CM-C2-M6', 'cot-2', 100000, 'CASH', '2026-01-01'),
('CM-C2-M7', 'cot-2', 40000, 'MOBILE_MONEY', '2026-01-01'),
('CM-C2-M8', 'cot-2', 60000, 'MOBILE_MONEY', '2026-01-01');

-- ============================================================================
-- STEP 1 : CLEANUP — keep only base data (collectivities, members,
--          memberships, referees, and original treasury accounts)
-- ============================================================================

DELETE FROM payment_receipt;
DELETE FROM dues_rule;
DELETE FROM mobile_money_account;
DELETE FROM bank_account;
DELETE FROM treasury_account;

-- ============================================================================
-- TREASURY ACCOUNTS  (original ones to keep)
-- ============================================================================

INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES
    ('C1-A-CASH',     'col-1', 'CASH',         0),
    ('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0),
    ('C2-A-CASH',     'col-2', 'CASH',         0),
    ('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0),
    ('C3-A-CASH',     'col-3', 'CASH',         0);

INSERT INTO mobile_money_account (treasury_account_id, account_holder_name, provider, phone_number)
VALUES
    ('C1-A-MOBILE-1', 'Mpanorina',      'ORANGE_MONEY', '0370489612'),
    ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612');

-- ============================================================================
-- STEP 2 : Update joined_at for all existing members to 01/01/2026
-- ============================================================================

UPDATE collectivity_membership
SET joined_at = '2026-01-01';

-- ============================================================================
-- 1) NEW FINANCIAL ACCOUNTS — collectivity 3
-- ============================================================================

-- Two bank accounts
INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES
    ('C3-A-BANK-1', 'col-3', 'BANK', 0),
    ('C3-A-BANK-2', 'col-3', 'BANK', 0);

INSERT INTO bank_account (
    treasury_account_id,
    account_holder_name,
    bank_name,
    bank_code,
    branch_code,
    account_number,
    rib_key
)
VALUES
    ('C3-A-BANK-1', 'Koto',  'BMOI', '00004', '00001', '12345678900', '12'),
    ('C3-A-BANK-2', 'Naivo', 'BRED', '00008', '00003', '45678901230', '58');

-- One mobile money account (MVOLA)
INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES
    ('C3-A-MOBILE-1', 'col-3', 'MOBILE_MONEY', 0);

INSERT INTO mobile_money_account (treasury_account_id, account_holder_name, provider, phone_number)
VALUES
    ('C3-A-MOBILE-1', 'Kolo', 'MVOLA', '0341889612');

-- ============================================================================
-- 2) DUES RULES
-- ============================================================================

-- Collectivity 1
INSERT INTO dues_rule (id, collectivity_id, frequency, amount_mga, label, effective_from)
VALUES
    ('cot-1', 'col-1', 'ANNUALLY',  200000, 'Cotisation annuelle', '2026-01-01'),
    ('cot-2', 'col-1', 'PUNCTUAL',   20000, 'Famangiana',          '2026-04-30');

-- Collectivity 2
INSERT INTO dues_rule (id, collectivity_id, frequency, amount_mga, label, effective_from, effective_to)
VALUES
    ('cot-3', 'col-2', 'ANNUALLY', 200000, 'Cotisation annuelle', '2026-01-01', NULL),
    ('cot-4', 'col-2', 'ANNUALLY', 100000, 'Cotisation 2025',     '2025-01-01', '2025-12-31');

-- Collectivity 3
INSERT INTO dues_rule (id, collectivity_id, frequency, amount_mga, label, effective_from)
VALUES
    ('cot-5', 'col-3', 'MONTHLY', 25000, 'Cotisation mensuelle', '2026-04-01');

-- ============================================================================
-- 3) PAYMENT RECEIPTS  (with treasury_account_id)
-- ============================================================================

-- --- Collectivity 1 (Tableau 15) -------------------------------------------

INSERT INTO payment_receipt (
    collectivity_membership_id, dues_rule_id, amount_mga,
    payment_method, treasury_account_id, collected_at
)
VALUES
    ('CM-C1-M1', 'cot-1', 200000, 'CASH',         'C1-A-CASH',     '2026-01-01'),
    ('CM-C1-M2', 'cot-1', 200000, 'CASH',         'C1-A-CASH',     '2026-01-01'),
    ('CM-C1-M3', 'cot-1', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
    ('CM-C1-M4', 'cot-1', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
    ('CM-C1-M5', 'cot-1', 150000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
    ('CM-C1-M6', 'cot-1', 100000, 'CASH',         'C1-A-CASH',     '2026-05-01'),
    ('CM-C1-M7', 'cot-1',  60000, 'CASH',         'C1-A-CASH',     '2026-05-01'),
    ('CM-C1-M8', 'cot-1',  90000, 'CASH',         'C1-A-CASH',     '2026-05-01');

-- --- Collectivity 2 (Tableau 16) -------------------------------------------

INSERT INTO payment_receipt (
    collectivity_membership_id, dues_rule_id, amount_mga,
    payment_method, treasury_account_id, collected_at
)
VALUES
    ('CM-C2-M1', 'cot-3', 120000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M2', 'cot-3', 180000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M3', 'cot-3', 200000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M4', 'cot-3', 200000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M5', 'cot-3', 200000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M6', 'cot-3', 200000, 'CASH',         'C2-A-CASH',     '2026-01-01'),
    ('CM-C2-M7', 'cot-3',  80000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', '2026-01-01'),
    ('CM-C2-M8', 'cot-3', 120000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', '2026-01-01');

-- --- Collectivity 3 (Tableau 17) -------------------------------------------
-- Note: C3-M3 and C3-M4 on 01/05 are listed as BANK method but credited to
--       C3-A-MOBILE-1; method kept as BANK per the source table, account as given.

INSERT INTO payment_receipt (
    collectivity_membership_id, dues_rule_id, amount_mga,
    payment_method, treasury_account_id, collected_at
)
VALUES
    -- April payments
    ('CM-C3-M1', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-04-01'),
    ('CM-C3-M2', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-04-01'),
    ('CM-C3-M3', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-04-01'),
    ('CM-C3-M4', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-04-01'),
    ('CM-C3-M5', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-2',   '2026-04-01'),
    ('CM-C3-M6', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-2',   '2026-04-01'),
    ('CM-C3-M7', 'cot-5', 25000, 'CASH',          'C3-A-CASH',     '2026-04-01'),
    ('CM-C3-M8', 'cot-5', 25000, 'CASH',          'C3-A-CASH',     '2026-04-01'),
    -- May payments
    ('CM-C3-M1', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-05-01'),
    ('CM-C3-M2', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1',   '2026-05-01'),
    ('CM-C3-M3', 'cot-5', 15000, 'BANK_TRANSFER', 'C3-A-MOBILE-1', '2026-05-01'),
    ('CM-C3-M4', 'cot-5', 15000, 'BANK_TRANSFER', 'C3-A-MOBILE-1', '2026-05-01'),
    ('CM-C3-M5', 'cot-5', 20000, 'BANK_TRANSFER', 'C3-A-BANK-2',   '2026-05-01'),
    ('CM-C3-M6', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-2',   '2026-05-01'),
    ('CM-C3-M7', 'cot-5',  5000, 'CASH',          'C3-A-CASH',     '2026-05-01'),
    ('CM-C3-M8', 'cot-5',  5000, 'CASH',          'C3-A-CASH',     '2026-05-01');

-- ============================================================================
-- 4) NEW JUNIOR MEMBERS  (<random> fields filled with plausible values)
-- ============================================================================

-- --- Collectivity 1 : 4 new juniors ----------------------------------------

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    ('C1-M9',  'Haingo',   'Rakoto',    '2000-03-15', 'MALE',   'Lot12A Ambatondrazaka', 'Riziculteur', '0341100001', 'haingo.rakoto@fed-agri.mg'),
    ('C1-M10', 'Miora',    'Rasolofo',  '2001-07-22', 'FEMALE', 'Lot45B Ambatondrazaka', 'Agriculteur', '0341100002', 'miora.rasolofo@fed-agri.mg'),
    ('C1-M11', 'Tojo',     'Andrianaly','2003-11-05', 'MALE',   'Lot78C Ambatondrazaka', 'Riziculteur', '0341100003', 'tojo.andrianaly@fed-agri.mg'),
    ('C1-M12', 'Fanja',    'Ramaroson', '1999-04-18', 'FEMALE', 'Lot90D Ambatondrazaka', 'Collecteur',  '0341100004', 'fanja.ramaroson@fed-agri.mg');

INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at)
VALUES
    ('CM-C1-M9',  'C1-M9',  'col-1', 'JUNIOR', '2026-04-01'),
    ('CM-C1-M10', 'C1-M10', 'col-1', 'JUNIOR', '2026-04-01'),
    ('CM-C1-M11', 'C1-M11', 'col-1', 'JUNIOR', '2026-05-01'),
    ('CM-C1-M12', 'C1-M12', 'col-1', 'JUNIOR', '2026-06-01');

INSERT INTO membership_referee (membership_id, referee_member_id, referee_collectivity_id, relationship_nature)
VALUES
    ('CM-C1-M9',  'C1-M1', 'col-1', NULL),
    ('CM-C1-M9',  'C1-M2', 'col-1', NULL),
    ('CM-C1-M10', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M10', 'C1-M2', 'col-1', NULL),
    ('CM-C1-M11', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M11', 'C1-M2', 'col-1', NULL),
    ('CM-C1-M12', 'C1-M1', 'col-1', NULL),
    ('CM-C1-M12', 'C1-M2', 'col-1', NULL);

-- --- Collectivity 2 : 3 new juniors ----------------------------------------

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    ('C2-M9',  'Ny Aina', 'Rabenja',    '2002-01-10', 'MALE',   'Lot11A Ambatondrazaka', 'Pisciculteur', '0341200001', 'nyaina.rabenja@fed-agri.mg'),
    ('C2-M10', 'Saholy',  'Rakotondr',  '2000-09-30', 'FEMALE', 'Lot22B Ambatondrazaka', 'Agriculteur',  '0341200002', 'saholy.rakotondr@fed-agri.mg'),
    ('C2-M11', 'Mendrika','Raharison',  '2001-05-14', 'MALE',   'Lot33C Ambatondrazaka', 'Pisciculteur', '0341200003', 'mendrika.raharison@fed-agri.mg');

INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at)
VALUES
    ('CM-C2-M9',  'C2-M9',  'col-2', 'JUNIOR', '2026-03-01'),
    ('CM-C2-M10', 'C2-M10', 'col-2', 'JUNIOR', '2026-03-01'),
    ('CM-C2-M11', 'C2-M11', 'col-2', 'JUNIOR', '2026-03-01');

INSERT INTO membership_referee (membership_id, referee_member_id, referee_collectivity_id, relationship_nature)
VALUES
    ('CM-C2-M9',  'C1-M1', 'col-1', NULL),
    ('CM-C2-M9',  'C1-M2', 'col-1', NULL),
    ('CM-C2-M10', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M10', 'C1-M2', 'col-1', NULL),
    ('CM-C2-M11', 'C1-M1', 'col-1', NULL),
    ('CM-C2-M11', 'C1-M2', 'col-1', NULL);

-- --- Collectivity 3 : 6 new juniors ----------------------------------------

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    ('C3-M9',  'Vatosoa',  'Rasoarimanana', '1998-05-20', 'FEMALE', 'Lot10A Brickaville', 'Apiculteur',  '0341300001', 'vatosoa.rasoarimanana@fed-agri.mg'),
    ('C3-M10', 'Hery',     'Andriamialy',   '2000-08-11', 'MALE',   'Lot20B Brickaville', 'Agriculteur', '0341300002', 'hery.andriamialy@fed-agri.mg'),
    ('C3-M11', 'Zo',       'Rabemananjara', '2001-02-28', 'MALE',   'Lot30C Brickaville', 'Apiculteur',  '0341300003', 'zo.rabemananjara@fed-agri.mg'),
    ('C3-M12', 'Lalaina',  'Rakotonirina',  '1999-12-01', 'FEMALE', 'Lot40D Brickaville', 'Collecteur',  '0341300004', 'lalaina.rakotonirina@fed-agri.mg'),
    ('C3-M13', 'Mamy',     'Randriamahefa', '2002-03-17', 'MALE',   'Lot50E Brickaville', 'Apiculteur',  '0341300005', 'mamy.randriamahefa@fed-agri.mg'),
    ('C3-M14', 'Tsanta',   'Rasolofonirina','2003-07-09', 'MALE',   'Lot60F Brickaville', 'Apiculteur',  '0341300006', 'tsanta.rasolofonirina@fed-agri.mg');

INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at)
VALUES
    ('CM-C3-M9',  'C3-M9',  'col-3', 'JUNIOR', '2026-01-01'),
    ('CM-C3-M10', 'C3-M10', 'col-3', 'JUNIOR', '2026-02-01'),
    ('CM-C3-M11', 'C3-M11', 'col-3', 'JUNIOR', '2026-02-01'),
    ('CM-C3-M12', 'C3-M12', 'col-3', 'JUNIOR', '2026-03-01'),
    ('CM-C3-M13', 'C3-M13', 'col-3', 'JUNIOR', '2026-03-01'),
    ('CM-C3-M14', 'C3-M14', 'col-3', 'JUNIOR', '2026-03-01');

INSERT INTO membership_referee (membership_id, referee_member_id, referee_collectivity_id, relationship_nature)
VALUES
    ('CM-C3-M9',  'C3-M1', 'col-3', NULL),
    ('CM-C3-M9',  'C3-M2', 'col-3', NULL),
    ('CM-C3-M10', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M10', 'C3-M2', 'col-3', NULL),
    ('CM-C3-M11', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M11', 'C3-M2', 'col-3', NULL),
    ('CM-C3-M12', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M12', 'C3-M2', 'col-3', NULL),
    ('CM-C3-M13', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M13', 'C3-M2', 'col-3', NULL),
    ('CM-C3-M14', 'C3-M1', 'col-3', NULL),
    ('CM-C3-M14', 'C3-M2', 'col-3', NULL);