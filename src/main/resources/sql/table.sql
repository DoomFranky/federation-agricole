\c federation_agricole_db

CREATE TABLE collectivity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) UNIQUE NOT NULL,
    location VARCHAR(100) NOT NULL,
    specialitation_agricole VARCHAR(100) NOT NULL,
    creation_date DATE DEFAULT CURRENT_DATE
);
CREATE TYPE gender as ENUM (
    'FEMALE','MALE'
)

CREATE TYPE occupation as ENUM (
    'president',
    'vicePresident',
    'treasurer',
    'secretary'
)

CREATE TABLE member (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lastName VARCHAR(100) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    gender gender,
    address TEXT,
    profession VARCHAR(100),
    phoneNumber VARCHAR(20),
    email VARCHAR(150) UNIQUE,
    adhesion_date DATE DEFAULT CURRENT_DATE,
    collectivity_id INTEGER REFERENCES collectivite(id),
    occupation occupation
);


CREATE TABLE referees (
    id SERIAL PRIMARY KEY,
    reference_id INTEGER REFERENCES member(id),
    referred_id INTEGER REFERENCES member(id),
    relation VARCHAR(100),
    date_referees TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    collectivity_id INTEGER REFERENCES collectivity(id),
    account_type VARCHAR(20) CHECK (type_compte IN ('caisse', 'banque', 'mobile_money')),
    referees_name VARCHAR(100), [cite: 147, 150]
    etablissement_name VARCHAR(50),
    account_number VARCHAR(23),
    solde_ariary DECIMAL(15, 2) DEFAULT 0.00 
);