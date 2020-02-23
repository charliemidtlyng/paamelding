create table endring (
id SERIAL,
hendelseid INTEGER,
deltakerid INTEGER,
endringstekst VARCHAR(255),
endringstidspunkt TIMESTAMP,
oppdatert BOOLEAN DEFAULT 'f'
);
