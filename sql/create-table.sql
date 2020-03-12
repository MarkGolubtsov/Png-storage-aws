CREATE TABLE image_meta_data
(
    "id"            SERIAL PRIMARY KEY,
    "name"          VARCHAR(1000),
    "creation_date" TIMESTAMP DEFAULT now(),
    "size"          INTEGER
)