

-- Create sequence for primary key
CREATE SEQUENCE wods_id_seq START WITH 1 INCREMENT BY 1;

-- Create the 'word' table
CREATE TABLE word (
    id BIGINT PRIMARY KEY DEFAULT nextval('wods_id_seq'),
    uuid UUID NOT NULL UNIQUE,
    name TEXT,
    base_form TEXT,
    important TEXT,
    kind_of_word TEXT,
    main_mean TEXT,
    base_lang_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version INT
);

-- Indexes
CREATE INDEX idx_word_uuid ON word (uuid);
CREATE INDEX idx_word_base_lang_id ON word (base_lang_id);

-- Foreign key constraint to 'language' table
ALTER TABLE word
    ADD CONSTRAINT fk_word_base_lang
    FOREIGN KEY (base_lang_id)
    REFERENCES language (id);



--liquibase formatted sql

-- changeset yourname:drop-mean-table
DROP TABLE IF EXISTS mean CASCADE;

-- changeset yourname:create-mean-table
CREATE SEQUENCE IF NOT EXISTS means_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE mean (
    id BIGINT DEFAULT nextval('means_id_seq') PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255),
    "order" INTEGER,
    created TIMESTAMP DEFAULT now() NOT NULL,
    updated TIMESTAMP DEFAULT now(),
    version INTEGER,
    base_lang_id BIGINT,
    base_word_id BIGINT
);

-- Add foreign keys (assuming Language and Word have "id" as PK)
ALTER TABLE mean
    ADD CONSTRAINT fk_mean_base_lang FOREIGN KEY (base_lang_id) REFERENCES language(id);

ALTER TABLE mean
    ADD CONSTRAINT fk_mean_base_word FOREIGN KEY (base_word_id) REFERENCES word(id);



-- Create sequence for primary key
CREATE SEQUENCE examples_id_seq START WITH 1 INCREMENT BY 1;

-- Create the 'examples' table
CREATE TABLE examples (
    id BIGINT PRIMARY KEY DEFAULT nextval('examples_id_seq'),
    uuid UUID NOT NULL UNIQUE,
    base_lang_id BIGINT,
    base_word_id BIGINT NOT NULL,
    name TEXT,
    "order" INT DEFAULT 0,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP WITHOUT TIME ZONE,
    version INT
);

-- Indexes
CREATE INDEX idx_examples_uuid ON examples (uuid);
CREATE INDEX idx_examples_order ON examples ("order");
CREATE INDEX idx_examples_base_lang_id ON examples (base_lang_id);
CREATE INDEX idx_examples_base_word_id ON examples (base_word_id);

-- Foreign key constraint to 'language' table
ALTER TABLE examples
    ADD CONSTRAINT fk_examples_base_lang
    FOREIGN KEY (base_lang_id)
    REFERENCES language (id);

-- Foreign key constraint to 'word' table
ALTER TABLE examples
    ADD CONSTRAINT fk_examples_base_word
    FOREIGN KEY (base_word_id)
    REFERENCES word (id);

    
    --liquibase formatted sql

-- changeset yourname:drop-file-entity-table
DROP TABLE IF EXISTS file_entity CASCADE;

-- changeset yourname:create-file-entity-table
CREATE SEQUENCE IF NOT EXISTS file_entity_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE file_entity (
    id BIGINT DEFAULT nextval('file_entity_id_seq') PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255),
    content_type VARCHAR(255),
    extension VARCHAR(255),
    file_name VARCHAR(255),
    original_file_name VARCHAR(255),
    sha256 VARCHAR(255),
    size BIGINT,
    created TIMESTAMP DEFAULT now() NOT NULL,
    updated TIMESTAMP DEFAULT now(),
    version INTEGER
);

--liquibase formatted sql

-- changeset yourname:drop-subtitle-file-table
DROP TABLE IF EXISTS subtitle_file CASCADE;

-- changeset yourname:create-subtitle-file-table
CREATE SEQUENCE IF NOT EXISTS subtitle_file_id START WITH 1 INCREMENT BY 1;

CREATE TABLE subtitle_file (
    id BIGINT DEFAULT nextval('subtitle_file_id') PRIMARY KEY,
    name VARCHAR(255),
    upload_date TIMESTAMP DEFAULT now() NOT NULL,
    base_lang_id BIGINT,
    file_id BIGINT UNIQUE,
    created TIMESTAMP DEFAULT now() NOT NULL,
    updated TIMESTAMP DEFAULT now(),
    version INTEGER
    
);

-- Add foreign key to Language
ALTER TABLE subtitle_file
    ADD CONSTRAINT fk_subtitle_file_base_lang FOREIGN KEY (base_lang_id) REFERENCES language(id);

-- Add foreign key to FileEntity
ALTER TABLE subtitle_file
    ADD CONSTRAINT fk_subtitle_file_file FOREIGN KEY (file_id) REFERENCES file_entity(id);


    --liquibase formatted sql

-- changeset yourname:drop-timestamp-row-table
DROP TABLE IF EXISTS timestamp_row CASCADE;

-- changeset yourname:create-timestamp-row-table
CREATE SEQUENCE IF NOT EXISTS timestamp_row_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE timestamp_row (
    id BIGINT DEFAULT nextval('timestamp_row_id_seq') PRIMARY KEY,
    created TIMESTAMP DEFAULT now() NOT NULL,
    start_time VARCHAR(255),
    end_time VARCHAR(255),
    time_in BIGINT,
    time_out BIGINT,
    text VARCHAR(255),
    updated TIMESTAMP DEFAULT now(),
    uuid UUID UNIQUE NOT NULL,
    version INTEGER,
    subtitle_file_id BIGINT
);

-- Add foreign key to SubtitleFile
ALTER TABLE timestamp_row
    ADD CONSTRAINT fk_timestamp_row_subtitle_file FOREIGN KEY (subtitle_file_id) REFERENCES subtitle_file(id);
    
--liquibase formatted sql

-- changeset yourname:drop-translated-word-table
DROP TABLE IF EXISTS translated_word CASCADE;

-- changeset yourname:create-translated-word-table
CREATE SEQUENCE IF NOT EXISTS translated_word_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE translated_word (
    id BIGINT DEFAULT nextval('translated_word_id_seq') PRIMARY KEY,
    name VARCHAR(255),
    translated_name VARCHAR(255),
    name_class VARCHAR(255),
    uuid_class UUID,
    base_lang_id BIGINT,
    target_lang_id BIGINT,
    uuid UUID UNIQUE NOT NULL,
    created TIMESTAMP DEFAULT now() NOT NULL,
    updated TIMESTAMP DEFAULT now(),
    version INTEGER
);

-- Add foreign key to Language (base_lang_id)
ALTER TABLE translated_word
    ADD CONSTRAINT fk_translated_word_base_lang FOREIGN KEY (base_lang_id) REFERENCES language(id);

-- Add foreign key to Language (target_lang_id)
ALTER TABLE translated_word
    ADD CONSTRAINT fk_translated_word_target_lang FOREIGN KEY (target_lang_id) REFERENCES language(id);

