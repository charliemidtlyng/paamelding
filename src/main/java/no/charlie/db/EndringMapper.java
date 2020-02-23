package no.charlie.db;

import no.charlie.domain.Endring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class EndringMapper implements RowMapper<Endring> {
    @Override public Endring map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Endring()
                .withId(rs.getInt("id"))
                .withHendelseid(rs.getInt("hendelseid"))
                .withDeltakerId(rs.getInt("deltakerid"))
                .withEndringstekst(rs.getString("endringstekst"))
                .withEndringstidspunkt(rs.getObject("endringstidspunkt", LocalDateTime.class));
    }
}
