BEGIN;

-- ---------------------------------------------------------------------------
-- FEDERATION & COLLECTIVITÉS (Page 11)
-- ---------------------------------------------------------------------------
INSERT INTO federation (name) VALUES ('Fédération Agricole de Madagascar');

INSERT INTO collectivity (id, number, name, location, agricultural_specialty, federation_id)
VALUES 
('col-1', 1, 'Collectivité 1', 'Antananarivo', 'Riziculture', (SELECT id FROM federation)),
('col-2', 2, 'Collectivité 2', 'Antsirabe', 'Maraîchage', (SELECT id FROM federation));

-- ---------------------------------------------------------------------------
-- COMPTES DE TRÉSORERIE (Tableau 3, Page 12)
-- ---------------------------------------------------------------------------
-- Collectivité 1
INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES 
('C1-A-CASH', 'col-1', 'CASH', 0),
('C1-A-BANK-1', 'col-1', 'BANK', 0),
('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0);

-- Collectivité 2
INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES 
('C2-A-CASH', 'col-2', 'CASH', 0),
('C2-A-BANK-1', 'col-2', 'BANK', 0),
('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0);

-- ---------------------------------------------------------------------------
-- MEMBRES (Tableaux 4 & 7, Pages 13-16)
-- ---------------------------------------------------------------------------
-- Quelques membres de la Collectivité 1
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES 
('C1-M1', 'Jean', 'Rakoto', '1985-01-01', 'MALE', 'Tana', 'Agriculteur', '0340000001', 'jean.rakoto@email.mg'),
('C1-M2', 'Soa', 'Andrianina', '1990-05-10', 'FEMALE', 'Tana', 'Gestionnaire', '0340000002', 'soa.a@email.mg'),
('C1-M3', 'Mamy', 'Randria', '1982-03-20', 'MALE', 'Tana', 'Exportateur', '0340000003', 'mamy.r@email.mg');

-- Quelques membres de la Collectivité 2
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES 
('C2-M1', 'Lova', 'Rabe', '1988-11-15', 'FEMALE', 'Antsirabe', 'Technicien', '0340000011', 'lova.rabe@email.mg'),
('C2-M2', 'Riry', 'Sitraka', '1992-07-25', 'MALE', 'Antsirabe', 'Cultivateur', '0340000012', 'riry.s@email.mg');

-- ---------------------------------------------------------------------------
-- ADHÉSIONS (MEMBERSHIPS)
-- ---------------------------------------------------------------------------
INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at)
VALUES 
('MS-C1-M1', 'C1-M1', 'col-1', 'PRESIDENT', '2025-01-01'),
('MS-C1-M2', 'C1-M2', 'col-1', 'TREASURER', '2025-01-05'),
('MS-C2-M1', 'C2-M1', 'col-2', 'PRESIDENT', '2025-02-01'),
('MS-C2-M2', 'C2-M2', 'col-2', 'SECRETARY', '2025-02-10');

-- ---------------------------------------------------------------------------
-- PAIEMENTS / ENCAISSEMENTS (Tableau 10, Page 19)
-- ---------------------------------------------------------------------------
-- Exemples de paiements pour la Collectivité 2
INSERT INTO payment_receipt (id, collectivity_membership_id, amount_mga, payment_method, collected_at)
VALUES 
('PAY-001', 'MS-C2-M1', 60000, 'CASH', '2026-01-01'),
('PAY-002', 'MS-C2-M2', 90000, 'CASH', '2026-01-01');

COMMIT;


mandate,treasury_account,activity,federation_report,federation_report_collectivity




-- Rétablissement des liens de clé étrangère
ALTER TABLE federation_mandate 
  ADD CONSTRAINT federation_mandate_federation_id_fkey 
  FOREIGN KEY (federation_id) REFERENCES federation(id);

ALTER TABLE treasury_account 
  ADD CONSTRAINT treasury_account_federation_id_fkey 
  FOREIGN KEY (federation_id) REFERENCES federation(id);

ALTER TABLE activity 
  ADD CONSTRAINT activity_federation_id_fkey 
  FOREIGN KEY (federation_id) REFERENCES federation(id);

ALTER TABLE federation_report 
  ADD CONSTRAINT federation_report_federation_id_fkey 
  FOREIGN KEY (federation_id) REFERENCES federation(id);