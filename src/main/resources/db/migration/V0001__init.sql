create table hendelse (
id SERIAL,
hendelsestype VARCHAR(255),
sted VARCHAR(255),
starttidspunkt TIMESTAMP,
paameldingstidspunkt TIMESTAMP,
varighet_minutter INTEGER,
maks_antall INTEGER,
lenke VARCHAR(1024),
info TEXT,
siste_slack_oppdatering TIMESTAMP
);

create table deltaker (
id SERIAL,
navn VARCHAR(255),
slacknavn VARCHAR(255),
registreringstidspunkt TIMESTAMP,
avmeldingstidspunkt TIMESTAMP,
hendelseId INTEGER,
uttatt BOOLEAN DEFAULT 't'
);


