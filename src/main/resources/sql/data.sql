-- Federation
INSERT INTO federation (id, name)
VALUES (1, 'Fédération Agricole');

-- Collectivity
INSERT INTO collectivity (id, number, name, location, agricultural_specialty, federation_id)
VALUES
    ('col-1', 1, 'Mpanorina', 'Ambatondrazaka', 'Riziculture', 1),
    ('col-2', 2, 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture', 1),
    ('col-3', 3, 'Tantely mamy', 'Brickaville', 'Apiculture', 1);

-- Members
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email)
VALUES
    ('m1','Prénom membre1','Nom membre1','1980-02-01','MALE','Lot IV Ambato','Riziculteur','0341234567','member.1@fed-agri.mg'),
    ('m2','Prénom membre2','Nom membre2','1982-03-05','MALE','Lot IF Ambato','Agriculteur','0321234567','member.2@fed-agri.mg'),
    ('m3','Prénom membre3','Nom membre3','1992-03-10','MALE','Lot IJ Ambato','Collecteur','0331234567','member.3@fed-agrim.g'),
    ('m4','Prénom membre4','Nom membre4','1988-05-22','FEMALE','Lot A K50 Ambato','Distributeur','0381234567','member.4@fed-agri.mg'),
    ('m5','Prénom membre5','Nom membre5','1999-08-21','MALE','Lot UV80 Ambato','Riziculteur','0373434567','member.5@fed-agri.mg'),
    ('m6','Prénom membre6','Nom membre6','1998-08-22','FEMALE','Lot UV6 Ambato','Riziculteur','0372234567','member.6@fed-agri.mg'),
    ('m7','Prénom membre7','Nom membre7','1998-01-31','MALE','Lot UV7 Ambato','Riziculteur','0374234567','member.7@fed-agri.mg'),
    ('m8','Prénom membre8','Nom membre8','1975-08-20','MALE','Lot UV8 Ambato','Riziculteur','0370234567','member.8@fed-agri.mg'),

    ('m9','Prénom membre9','Nom membre9','1988-01-02','MALE','Lot33J Antsirabe','Apiculteur','034034567','member.9@fed-agri.mg'),
    ('m10','Prénom membre10','Nom membre10','1982-03-05','MALE','Lot2J Antsirabe','Agriculteur','0338634567','member.10@fed-agri.mg'),
    ('m11','Prénom membre11','Nom membre11','1992-03-12','MALE','Lot8KM Antsirabe','Collecteur','0338234567','member.11@fed-agrim.g'),
    ('m12','Prénom membre12','Nom membre12','1988-05-10','FEMALE','Lot A K50 Antsirabe','Distributeur','0382334567','member.12@fed-agri.mg'),
    ('m13','Prénom membre13','Nom membre13','1999-08-11','MALE','Lot UV80 Antsirabe','Apiculteur','0373365567','member.13@fed-agri.mg'),
    ('m14','Prénom membre14','Nom membre14','1998-08-09','FEMALE','Lot UV6 Antsirabe','Apiculteur','0378234567','member.14@fed-agri.mg'),
    ('m15','Prénom membre15','Nom membre15','1998-01-13','MALE','Lot UV7 Antsirabe','Apiculteur','0374914567','member.15@fed-agri.mg'),
    ('m16','Prénom membre16','Nom membre16','1975-08-02','MALE','Lot UV8 Antsirabe','Apiculteur','0370634567','member.16@fed-agri.mg');

-- Collectivity membership 1
INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation)
VALUES
    ('c1-m1','m1','col-1','PRESIDENT'),
    ('c1-m2','m2','col-1','VICE_PRESIDENT'),
    ('c1-m3','m3','col-1','SECRETARY'),
    ('c1-m4','m4','col-1','TREASURER'),
    ('c1-m5','m5','col-1','SENIOR'),
    ('c1-m6','m6','col-1','SENIOR'),
    ('c1-m7','m7','col-1','SENIOR'),
    ('c1-m8','m8','col-1','SENIOR');

-- Collectivity membership 2
INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation)
VALUES
    ('c2-m1','m1','col-2','SENIOR'),
    ('c2-m2','m2','col-2','SENIOR'),
    ('c2-m3','m3','col-2','SENIOR'),
    ('c2-m4','m4','col-2','SENIOR'),
    ('c2-m5','m5','col-2','PRESIDENT'),
    ('c2-m6','m6','col-2','VICE_PRESIDENT'),
    ('c2-m7','m7','col-2','SECRETARY'),
    ('c2-m8','m8','col-2','TREASURER');

-- Collectivity membership 3
INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation)
VALUES
    ('c3-m1','m9','col-3','PRESIDENT'),
    ('c3-m2','m10','col-3','VICE_PRESIDENT'),
    ('c3-m3','m11','col-3','SECRETARY'),
    ('c3-m4','m12','col-3','TREASURER'),
    ('c3-m5','m13','col-3','SENIOR'),
    ('c3-m6','m14','col-3','SENIOR'),
    ('c3-m7','m15','col-3','SENIOR'),
    ('c3-m8','m16','col-3','SENIOR');


-- Referees
INSERT INTO membership_referee (membership_id, referee_member_id, referee_collectivity_id)
VALUES
    ('c1-m3','m1','col-1'),
    ('c1-m3','m2','col-1'),
    ('c1-m4','m1','col-1'),
    ('c1-m4','m2','col-1'),

    ('c1-m5','m1','col-1'),
    ('c1-m5','m2','col-1'),
    ('c1-m6','m1','col-1'),
    ('c1-m6','m2','col-1'),

    ('c1-m7','m1','col-1'),
    ('c1-m7','m2','col-1'),
    ('c1-m8','m6','col-1'),
    ('c1-m8','m7','col-1');

-- Dues
INSERT INTO dues_rule (id, collectivity_id, frequency, amount_mga, label, effective_from)
VALUES
    ('cot-1','col-1','ANNUALLY',100000,'Cotisation annuelle','2026-01-01'),
    ('cot-2','col-2','ANNUALLY',100000,'Cotisation annuelle','2026-01-01'),
    ('cot-3','col-3','ANNUALLY',50000,'Cotisation annuelle','2026-01-01');


-- Treasury account
INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga)
VALUES
    ('c1-cash','col-1','CASH',0),
    ('c2-cash','col-2','CASH',0),
    ('c3-cash','col-3','CASH',0),

    ('c1-mm','col-1','MOBILE_MONEY',0),
    ('c2-mm','col-2','MOBILE_MONEY',0);

INSERT INTO mobile_money_account (treasury_account_id, account_holder_name, provider, phone_number)
VALUES
    ('c1-mm','Mpanorina','ORANGE_MONEY','0370489612'),
    ('c2-mm','Dobo voalohany','ORANGE_MONEY','0320489612');


-- Payements
INSERT INTO payment_receipt (collectivity_membership_id, dues_rule_id, amount_mga, payment_method, collected_at)
VALUES
    ('c1-m1','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m2','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m3','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m4','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m5','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m6','cot-1',100000,'CASH','2026-01-01'),
    ('c1-m7','cot-1',60000,'CASH','2026-01-01'),
    ('c1-m8','cot-1',90000,'CASH','2026-01-01');