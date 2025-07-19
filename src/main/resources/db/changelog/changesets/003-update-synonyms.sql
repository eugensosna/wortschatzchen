
-- Create sequence for primary key
CREATE SEQUENCE synonyms_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE synonyms (
     id BIGINT PRIMARY KEY DEFAULT nextval('synonyms_id_seq'), -- Replace with your actual sequence if needed
    base_lang_id BIGINT,
    base_word_id BIGINT,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255),
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uuid UUID NOT NULL UNIQUE,
    version INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE synonyms
    ADD CONSTRAINT fk_synonyms_base_lang FOREIGN KEY (base_lang_id) REFERENCES language(id);

ALTER TABLE synonyms
    ADD CONSTRAINT fk_synonyms_base_word FOREIGN KEY (base_word_id) REFERENCES word(id);
