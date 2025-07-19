ALTER TABLE subtitle_file
ADD COLUMN uuid UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE;

CREATE INDEX subtitle_file_uuid_idx ON subtitle_file (uuid);