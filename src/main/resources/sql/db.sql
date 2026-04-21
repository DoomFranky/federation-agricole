CREATE USER agricultural_federation_user WITH PASSWORD '123456';

CREATE DATABASE agricultural_federation_db OWNER agricultural_federation_user;

GRANT ALL PRIVILEGES ON DATABASE agricultural_federation_db TO agricultural_federation_user;