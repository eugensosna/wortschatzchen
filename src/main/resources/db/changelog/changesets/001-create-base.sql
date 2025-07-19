
-- Drop existing table and sequence if they exist
DROP TABLE IF EXISTS language CASCADE;
DROP SEQUENCE IF EXISTS sequenceLanguage;
drop index if exists idx_language_uuid;


-- Create sequence for primary key
CREATE SEQUENCE sequence_language START WITH 1 INCREMENT BY 1;

-- Create the 'language' table
CREATE TABLE language (
    id BIGINT PRIMARY KEY DEFAULT nextval('sequence_language'),
    uuid UUID NOT NULL UNIQUE,
    name TEXT,
    short_name TEXT UNIQUE,
    flag_id TEXT,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version INT
);

-- Indexes
CREATE INDEX idx_language_uuid ON language (uuid);
CREATE INDEX idx_language_short_name ON language (short_name);
