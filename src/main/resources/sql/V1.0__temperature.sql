--schema
CREATE SCHEMA IF NOT EXISTS doghouse AUTHORIZATION root;

--sequences
CREATE SEQUENCE IF NOT EXISTS doghouse.temperatures_seq start with 1 cache 50 increment 1 cycle;

--pg_history_change table
CREATE TABLE IF NOT EXISTS doghouse.temperatures
(
    t_id bigint NOT NULL DEFAULT nextval('doghouse.temperatures_seq'),
    t_date timestamp with time zone NOT NULL,
    t_outside float NOT NULL,
    t_inside_happy float NOT NULL,
    t_inside_snoopy float NOT NULL,
    t_is_open_happy boolean NOT NULL,
    t_is_open_snoopy boolean NOT NULL,

    CONSTRAINT temperatures_pkey PRIMARY KEY (t_id)
)
WITH (
         OIDS = FALSE
     );

CREATE INDEX IF NOT EXISTS t_idx ON doghouse.temperatures (t_id);
CREATE INDEX IF NOT EXISTS t_datex ON doghouse.temperatures (t_date);
