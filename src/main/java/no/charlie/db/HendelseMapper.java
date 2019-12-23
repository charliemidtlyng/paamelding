package no.charlie.db;

import no.charlie.domain.Hendelse;
import no.charlie.domain.Hendelsestype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class HendelseMapper implements RowMapper<Hendelse> {
    @Override public Hendelse map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Hendelse()
                .withId(rs.getInt("id"))
                .withSted(rs.getString("sted"))
                .withHendelsestype(Hendelsestype.valueOf(rs.getString("hendelsestype")))
                .withStarttid(rs.getObject("starttidspunkt", LocalDateTime.class))
                .withPaameldingstid(rs.getObject("paameldingstidspunkt", LocalDateTime.class))
                .withVarighet(rs.getInt("varighet_minutter"))
                .withLenke(rs.getString("lenke"))
                .withInfo(rs.getString("info"))
                .withMaksAntallDeltakere(rs.getInt("maks_antall"))
                .withSisteSlackOppdatering(rs.getObject("siste_slack_oppdatering", LocalDateTime.class));
    }
}
