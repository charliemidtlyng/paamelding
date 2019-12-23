package no.charlie.db;

import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;
import no.charlie.domain.Hendelsestype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class DeltakerMapper implements RowMapper<Deltaker> {
    @Override public Deltaker map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Deltaker()
                .withId(rs.getInt("id"))
                .withNavn(rs.getString("navn"))
                .withSlacknavn(rs.getString("slacknavn"))
                .withRegistreringstidspunkt(rs.getObject("registreringstidspunkt", LocalDateTime.class))
                .withAvmeldingstidspunkt(rs.getObject("avmeldingstidspunkt", LocalDateTime.class))
                .withHendelseid(rs.getInt("hendelseid"))
                .withUttatt(rs.getBoolean("uttatt"));
    }
}
