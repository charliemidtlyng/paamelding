package no.charlie.db;

import no.charlie.domain.Deltaker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.config.KeyColumn;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.config.ValueColumn;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface DeltakerDAO {

    @SqlUpdate("insert into deltaker (navn, slacknavn, registreringstidspunkt, avmeldingstidspunkt, hendelseid, uttatt) " +
            "values " +
            "(:navn, :slacknavn, :registreringstidspunkt, :avmeldingstidspunkt, :hendelseid, 'f')")
    @GetGeneratedKeys("id")
    int meldPaa(@Bind("navn") String navn,
                @Bind("slacknavn") String slacknavn,
                @Bind("registreringstidspunkt") LocalDateTime registreringstid,
                @Bind("avmeldingstidspunkt") LocalDateTime avmeldingstid,
                @Bind("hendelseid") int hendelse
    );

    @SqlQuery("select id, navn, slacknavn, registreringstidspunkt, avmeldingstidspunkt, hendelseid, uttatt from deltaker where hendelseid" +
            " = :hendelseid")
    @RegisterRowMapper(DeltakerMapper.class)
    List<Deltaker> finnDeltakereForHendelse(@Bind("hendelseid") int hendelseId);

    @SqlUpdate("update deltaker set uttatt = 't' where id in (<deltakerIder>)")
    void settUttatt(@BindList("deltakerIder") List<Integer> deltakerIder);

    @SqlUpdate("update deltaker set uttatt = 'f', avmeldingstidspunkt = :avmeldingstidspunkt where id = :deltakerId")
    void meldAv(@Bind("deltakerId") Integer deltakerId, @Bind("avmeldingstidspunkt") LocalDateTime avmeldingstidspunkt);

    @SqlQuery("select hendelseid, count(*) as antall from deltaker where avmeldingstidspunkt is null and hendelseid in ( <hendelsesider> " +
            ") group by hendelseid")
    @KeyColumn("hendelseid")
    @ValueColumn("antall")
    Map<Integer, Integer> finnDeltakerAntallForHendelser(@BindList("hendelsesider") List<Integer> hendelsesider);
}
