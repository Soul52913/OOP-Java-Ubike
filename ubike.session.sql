CREATE DATABASE IF NOT EXISTS ubike;

CREATE TABLE IF NOT EXISTS users (
    phone_number VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    id_number VARCHAR(255),
    email VARCHAR(255),
    card_number VARCHAR(255)
    PRIMARY KEY (phone_number, id_number, email, card_number)
);

CREATE TABLE IF NOT EXISTS cards (
    card_id VARCHAR(255) PRIMARY KEY,
    amount DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS transactions (
    card_id VARCHAR(255),
    transaction_type VARCHAR(255),
    transaction_amount DECIMAL(10, 2),
    transaction_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS records (
    recordID VARCHAR(255) PRIMARY KEY,
    id_number VARCHAR(255),
    start VARCHAR(255),
    startTime DATETIME,
    end VARCHAR(255),
    endTime DATETIME,
    bikeID VARCHAR(255),
    amount DECIMAL(10, 2),
    cardID VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS stations (
    stationID VARCHAR(255) PRIMARY KEY,
    stationName VARCHAR(255),
    areaName VARCHAR(255),
    posX FLOAT,
    posY FLOAT,
    pillarList TEXT
);

CREATE TABLE IF NOT EXISTS pillars (
    stationID VARCHAR(255),
    pillarID VARCHAR(255) PRIMARY KEY,
    bikeID VARCHAR(255),
    maintenanceReport TEXT
);

CREATE TABLE IF NOT EXISTS bicycles (
    bikeID VARCHAR(255) PRIMARY KEY,
    bikeType INT,
    status INT,
    areaName VARCHAR(255),
    maintenanceReport TEXT
);


INSERT INTO pillars (stationID, pillarID, bikeID, maintenanceReport)
VALUES (
    'HSZ500401001',
    'HSZ500401001_01',
    '',
    ''
  );

-- SELECT * FROM pillars
-- WHERE bikeID = 'A00474';
-- WHERE stationID = 'TPE500101029';

-- SELECT * FROM bicycles
-- WHERE bikeID = 'A00474';