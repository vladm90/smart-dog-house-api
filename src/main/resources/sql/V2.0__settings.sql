

--sequences
CREATE SEQUENCE IF NOT EXISTS settings_seq start with 1 cache 50 increment 1 cycle;


CREATE TABLE IF NOT EXISTS settings
(
    s_id bigint NOT NULL DEFAULT nextval('settings_seq'),
    s_key text NOT NULL,
    s_value text NOT NULL,


    CONSTRAINT settings_pkey PRIMARY KEY (s_id)
)
WITH (
         OIDS = FALSE
     );

CREATE INDEX IF NOT EXISTS s_keyx ON temperatures (s_key);

/*
insert into settings (s_key, s_value) values (mode, auto);
insert into settings (s_key, s_value) values (temperature, 10);
select * from settings;*/