\c federation_agricole_db

-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";   -- gen_random_uuid()

-- ---------------------------------------------------------------------------
-- ENUMS
-- ---------------------------------------------------------------------------

CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

CREATE TYPE member_occupation AS ENUM (
    'JUNIOR',
    'SENIOR',
    'SECRETARY',
    'TREASURER',
    'VICE_PRESIDENT',
    'PRESIDENT'
    );

CREATE TYPE payment_method AS ENUM (
    'CASH',
    'BANK_TRANSFER',
    'MOBILE_MONEY'
    );

CREATE TYPE dues_frequency AS ENUM (
    'MONTHLY',
    'ANNUAL',
    'PUNCTUAL'
    );

CREATE TYPE account_type AS ENUM (
    'CASH',
    'BANK',
    'MOBILE_MONEY'
    );

CREATE TYPE mobile_money_provider AS ENUM (
    'ORANGE_MONEY',
    'MVOLA',
    'AIRTEL_MONEY'
    );

CREATE TYPE bank_name AS ENUM (
    'BRED',
    'MCB',
    'BMOI',
    'BOA',
    'BGFI',
    'AFG',
    'ACCES_BANQUE',
    'BAOBAB',
    'SIPEM'
    );

CREATE TYPE activity_scope AS ENUM (
    'COLLECTIVITY',
    'FEDERATION'
    );

CREATE TYPE activity_type AS ENUM (
    'MONTHLY_GENERAL_ASSEMBLY',
    'JUNIOR_TRAINING',
    'EXCEPTIONAL'
    );

-- ---------------------------------------------------------------------------
-- FEDERATION  (singleton – exactly one row expected)
-- ---------------------------------------------------------------------------

CREATE TABLE federation (
                            id             INTEGER PRIMARY KEY,
                            name            TEXT NOT NULL,
                            created_at      DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------------
-- COLLECTIVITIES
-- ---------------------------------------------------------------------------

CREATE TABLE collectivity (
                              id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              number              INTEGER,        -- human-readable unique number
                              name                TEXT,
                              location            TEXT NOT NULL,                 -- city / locality
                              agricultural_specialty TEXT,
                              created_at          DATE NOT NULL DEFAULT CURRENT_DATE,
                              federation_id       INTEGER NOT NULL REFERENCES federation(id)
);

-- ---------------------------------------------------------------------------
-- MEMBERS
-- core personal information, independent of any collectivity membership
-- ---------------------------------------------------------------------------

CREATE TABLE member (
                        id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        first_name      TEXT NOT NULL,
                        last_name       TEXT NOT NULL,
                        birth_date      DATE NOT NULL,
                        gender          gender NOT NULL,
                        address         TEXT NOT NULL,
                        profession      TEXT NOT NULL,
                        phone_number    VARCHAR(20) NOT NULL,
                        email           TEXT NOT NULL UNIQUE,
                        created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------------
-- COLLECTIVITY MEMBERSHIPS
-- A member belongs to one collectivity at a time (active), but history is kept.
-- ---------------------------------------------------------------------------

CREATE TABLE collectivity_membership (
                                         id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         member_id           UUID NOT NULL REFERENCES member(id),
                                         collectivity_id     UUID NOT NULL REFERENCES collectivity(id),
                                         occupation          member_occupation NOT NULL DEFAULT 'JUNIOR',
                                         joined_at           DATE NOT NULL DEFAULT CURRENT_DATE,
                                         left_at             DATE,                          -- NULL = still active
                                         resignation         BOOLEAN NOT NULL DEFAULT FALSE,
                                         CONSTRAINT no_overlap CHECK (left_at IS NULL OR left_at > joined_at)
);

-- Unique active membership per member (only one NULL left_at per member)
-- CREATE UNIQUE INDEX uq_active_membership
--     ON collectivity_membership (member_id)
--     WHERE left_at IS NULL;
--
-- ---------------------------------------------------------------------------
-- B-2 : REFEREES / SPONSORS
-- A new applicant must be sponsored by ≥2 confirmed members.
-- We store each (applicant, referee) pair plus the nature of the relationship
-- and which collectivity the referee belongs to (for the ≥ parity rule).
-- ---------------------------------------------------------------------------

CREATE TABLE membership_referee (
                                    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    membership_id           UUID NOT NULL REFERENCES collectivity_membership(id) ON DELETE CASCADE,
                                    referee_member_id       UUID NOT NULL REFERENCES member(id),
    -- collectivity of the referee at the time of application (denormalised for rule check)
                                    referee_collectivity_id UUID NOT NULL REFERENCES collectivity(id),
                                    relationship_nature     TEXT NOT NULL,             -- e.g. 'family', 'friend', 'colleague'

                                    UNIQUE (membership_id, referee_member_id)
);

-- ---------------------------------------------------------------------------
-- MANDATES  (postes spécifiques per year per collectivity)
-- One row per (collectivity, year, occupation) for PRESIDENT / VICE_PRESIDENT /
-- TREASURER / SECRETARY.  Enforces the ≤2 mandates per member per role rule.
-- ---------------------------------------------------------------------------

CREATE TABLE collectivity_mandate (
                                      id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      collectivity_id UUID NOT NULL REFERENCES collectivity(id),
                                      member_id       UUID NOT NULL REFERENCES member(id),
                                      occupation      member_occupation NOT NULL
                                          CHECK (occupation IN ('PRESIDENT','VICE_PRESIDENT','TREASURER','SECRETARY')),
                                      date            DATE NOT NULL DEFAULT CURRENT_DATE,

                                      UNIQUE (collectivity_id, occupation, year)        -- one holder per role per year
);

-- Enforce the max 2 mandates per (member, collectivity, occupation) rule via constraint
CREATE UNIQUE INDEX uq_mandate_count
    ON collectivity_mandate (collectivity_id, member_id, occupation, year);

-- (The "≤2 mandates" business rule is enforced in application logic / trigger)

-- ---------------------------------------------------------------------------
-- FEDERATION MANDATES  (2-year mandates)
-- ---------------------------------------------------------------------------

CREATE TABLE federation_mandate (
                                    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    federation_id UUID NOT NULL REFERENCES federation(id),
                                    member_id   UUID NOT NULL REFERENCES member(id),
                                    occupation  member_occupation NOT NULL
                                        CHECK (occupation IN ('PRESIDENT','VICE_PRESIDENT','TREASURER','SECRETARY')),
                                    start_year  SMALLINT NOT NULL,
                                    end_year    SMALLINT NOT NULL,
                                    CHECK (end_year = start_year + 1),

                                    UNIQUE (federation_id, occupation, start_year)
);

-- ---------------------------------------------------------------------------
-- DUES RULES
-- ---------------------------------------------------------------------------

CREATE TABLE dues_rule (
                           id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           collectivity_id UUID NOT NULL REFERENCES collectivity(id),
                           frequency       dues_frequency NOT NULL,
                           amount_mga      NUMERIC(12,2) NOT NULL CHECK (amount_mga > 0),
                           label           TEXT,                              -- e.g. "Cotisation annuelle obligatoire"
                           effective_from  DATE NOT NULL DEFAULT CURRENT_DATE,
                           effective_to    DATE,                              -- NULL = still in force

                           CHECK (effective_to IS NULL OR effective_to > effective_from)
);

-- ---------------------------------------------------------------------------
-- PAYMENT RECEIPTS  (encaissements)
-- ---------------------------------------------------------------------------

CREATE TABLE payment_receipt (
                                 id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 collectivity_membership_id UUID NOT NULL REFERENCES collectivity_membership(id),
                                 dues_rule_id            UUID REFERENCES dues_rule(id),   -- NULL for ad-hoc payments
                                 amount_mga              NUMERIC(12,2) NOT NULL CHECK (amount_mga > 0),
                                 payment_method          payment_method NOT NULL,
                                 collected_at            DATE NOT NULL DEFAULT CURRENT_DATE,
                                 collected_by_treasurer  UUID REFERENCES member(id),      -- treasurer who recorded it
                                 notes                   TEXT
);

-- ---------------------------------------------------------------------------
-- TREASURY ACCOUNTS
-- ---------------------------------------------------------------------------

CREATE TABLE treasury_account (
                                  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    -- Belongs to either a collectivity OR the federation (exactly one must be set)
                                  collectivity_id UUID REFERENCES collectivity(id),
                                  federation_id   UUID REFERENCES federation(id),
                                  account_type    account_type NOT NULL,
                                  balance_mga     NUMERIC(14,2) NOT NULL DEFAULT 0,
                                  currency        CHAR(3) NOT NULL DEFAULT 'MGA',
                                  as_of_date      DATE NOT NULL DEFAULT CURRENT_DATE,

                                  CONSTRAINT owner_xor CHECK (
                                      (collectivity_id IS NOT NULL AND federation_id IS NULL) OR
                                      (collectivity_id IS NULL AND federation_id IS NOT NULL)
                                      )
);

-- Only one CASH account per owner
CREATE UNIQUE INDEX uq_cash_per_collectivity
    ON treasury_account (collectivity_id)
    WHERE account_type = 'CASH' AND collectivity_id IS NOT NULL;

CREATE UNIQUE INDEX uq_cash_per_federation
    ON treasury_account (federation_id)
    WHERE account_type = 'CASH' AND federation_id IS NOT NULL;

-- ---------------------------------------------------------------------------
-- BANK ACCOUNTS  (detail for account_type = 'BANK')
-- ---------------------------------------------------------------------------

CREATE TABLE bank_account (
                              treasury_account_id UUID PRIMARY KEY REFERENCES treasury_account(id) ON DELETE CASCADE,
                              account_holder_name TEXT NOT NULL,
                              bank_name           bank_name NOT NULL,
    -- RIB format BBBBBGGGGGCCCCCCCCCCCKKK (23 digits)
                              bank_code           CHAR(5)  NOT NULL CHECK (bank_code ~ '^\d{5}$'),
                              branch_code         CHAR(5)  NOT NULL CHECK (branch_code ~ '^\d{5}$'),
                              account_number      CHAR(11) NOT NULL CHECK (account_number ~ '^\d{11}$'),
                              rib_key             CHAR(2)  NOT NULL CHECK (rib_key ~ '^\d{2}$')
);

-- ---------------------------------------------------------------------------
-- MOBILE MONEY ACCOUNTS  (detail for account_type = 'MOBILE_MONEY')
-- ---------------------------------------------------------------------------

CREATE TABLE mobile_money_account (
                                      treasury_account_id UUID PRIMARY KEY REFERENCES treasury_account(id) ON DELETE CASCADE,
                                      account_holder_name TEXT NOT NULL,
                                      provider            mobile_money_provider NOT NULL,
                                      phone_number        VARCHAR(20) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------------
-- ACTIVITIES
-- ---------------------------------------------------------------------------

CREATE TABLE activity (
                          id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          collectivity_id UUID REFERENCES collectivity(id),   -- NULL if federation-level
                          federation_id   UUID REFERENCES federation(id),
                          scope           activity_scope NOT NULL,
                          activity_type   activity_type NOT NULL,
                          title           TEXT NOT NULL,
                          description     TEXT,
                          scheduled_at    TIMESTAMPTZ NOT NULL,
                          is_mandatory    BOOLEAN NOT NULL DEFAULT TRUE,
    -- For recurring activities: anchor day description (e.g. "2nd Sunday of the month")
                          recurrence_rule TEXT,

                          CONSTRAINT activity_owner_xor CHECK (
                              (scope = 'COLLECTIVITY' AND collectivity_id IS NOT NULL AND federation_id IS NULL) OR
                              (scope = 'FEDERATION'   AND federation_id   IS NOT NULL AND collectivity_id IS NULL)
                              )
);

-- Targeted mandatory presence: which occupations must attend (NULL = all)
CREATE TABLE activity_target_occupation (
                                            activity_id UUID NOT NULL REFERENCES activity(id) ON DELETE CASCADE,
                                            occupation  member_occupation NOT NULL,
                                            PRIMARY KEY (activity_id, occupation)
);

-- ---------------------------------------------------------------------------
-- ATTENDANCE  (fiche de présence)
-- ---------------------------------------------------------------------------

CREATE TABLE attendance (
                            id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            activity_id     UUID NOT NULL REFERENCES activity(id),
                            member_id       UUID NOT NULL REFERENCES member(id),
    -- Home collectivity at time of activity (for assiduity calculation scope)
                            home_collectivity_id UUID REFERENCES collectivity(id),
                            is_present      BOOLEAN NOT NULL,
                            is_excused      BOOLEAN NOT NULL DEFAULT FALSE,
                            excuse_note     TEXT,
    -- TRUE if this member is a guest from another collectivity
                            is_guest        BOOLEAN NOT NULL DEFAULT FALSE,
                            recorded_by     UUID REFERENCES member(id),        -- secretary who recorded

                            UNIQUE (activity_id, member_id)
);

-- ---------------------------------------------------------------------------
-- FEDERATION STATISTICS REPORTS  (H)
-- Monthly/annual snapshots generated by the federation secretary
-- ---------------------------------------------------------------------------

CREATE TABLE federation_report (
                                   id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   federation_id       UUID NOT NULL REFERENCES federation(id),
                                   generated_by        UUID REFERENCES member(id),     -- secretary
                                   period_start        DATE NOT NULL,
                                   period_end          DATE NOT NULL,
                                   generated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),

                                   CHECK (period_end > period_start)
);

CREATE TABLE federation_report_collectivity (
                                                id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                report_id               UUID NOT NULL REFERENCES federation_report(id) ON DELETE CASCADE,
                                                collectivity_id         UUID NOT NULL REFERENCES collectivity(id),
                                                attendance_rate         NUMERIC(5,2) CHECK (attendance_rate BETWEEN 0 AND 100),
                                                dues_compliance_pct     NUMERIC(5,2) CHECK (dues_compliance_pct BETWEEN 0 AND 100),
                                                new_members_count       INTEGER NOT NULL DEFAULT 0,

                                                UNIQUE (report_id, collectivity_id)
);

-- =============================================================================
-- KEY INDEXES
-- =============================================================================

CREATE INDEX idx_membership_collectivity ON collectivity_membership (collectivity_id);
CREATE INDEX idx_membership_member       ON collectivity_membership (member_id);
CREATE INDEX idx_referee_membership      ON membership_referee (membership_id);
CREATE INDEX idx_referee_member          ON membership_referee (referee_member_id);
CREATE INDEX idx_payment_membership      ON payment_receipt (collectivity_membership_id);
CREATE INDEX idx_attendance_activity     ON attendance (activity_id);
CREATE INDEX idx_attendance_member       ON attendance (member_id);
CREATE INDEX idx_activity_collectivity   ON activity (collectivity_id);
CREATE INDEX idx_mandate_member          ON collectivity_mandate (member_id, collectivity_id, occupation);