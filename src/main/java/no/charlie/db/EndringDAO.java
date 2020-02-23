package no.charlie.db;

import no.charlie.domain.Endring;

import java.time.LocalDateTime;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface EndringDAO {

    @SqlUpdate("insert into endring (hendelseid, deltakerid, endringstekst, endringstidspunkt) " +
            "values " +
            "(:hendelseid, :deltakerid, :endringstekst, :endringstidspunkt)")
    @GetGeneratedKeys("id")
    int opprettEndring(@Bind("hendelseid") int hendelseid,
                       @Bind("deltakerid") int deltakerid,
                       @Bind("endringstekst") String endringstekst,
                       @Bind("endringstidspunkt") LocalDateTime endringstidspunkt
    );

    @SqlQuery("select id, hendelseid, deltakerid, endringstekst, endringstidspunkt from endring where oppdatert = 'f'")
    @RegisterRowMapper(EndringMapper.class)
    List<Endring> finnNyeEndringer();

    @SqlUpdate("update endring set oppdatert = 't' where id = :id")
    void settOppdatert(@Bind("id") int id);

}
