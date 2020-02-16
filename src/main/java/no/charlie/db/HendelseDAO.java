package no.charlie.db;

import no.charlie.domain.Hendelse;
import no.charlie.domain.Hendelsestype;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface HendelseDAO {

    @SqlUpdate("insert into hendelse (sted, hendelsestype, starttidspunkt, paameldingstidspunkt, varighet_minutter, lenke, info, maks_antall) " +
            "values " +
            "(:sted, :hendelsestype, :starttidspunkt, :paameldingstidspunkt, :varighet_minutter, :lenke, :info, :maks_antall)")
    @GetGeneratedKeys("id")
    int leggTilHendelse(@Bind("sted") String sted,
                @Bind("hendelsestype") Hendelsestype hendelsestype,
                @Bind("starttidspunkt") LocalDateTime starttid,
                @Bind("paameldingstidspunkt") LocalDateTime paameldingstid,
                @Bind("varighet_minutter") int varighet,
                @Bind("maks_antall") int antall,
                @Bind("lenke") String lenke,
                @Bind("info") String info
                );

    @SqlQuery("select id, sted, hendelsestype, starttidspunkt, paameldingstidspunkt, varighet_minutter, lenke, info, siste_slack_oppdatering, maks_antall from hendelse where id = :id")
    @RegisterRowMapper(HendelseMapper.class)
    Optional<Hendelse> finnHendelse(@Bind("id") int id);

    @SqlQuery("select id, sted, hendelsestype, starttidspunkt, paameldingstidspunkt, varighet_minutter, lenke, info, siste_slack_oppdatering, maks_antall from hendelse where hendelsestype in( <hendelsestyper> ) and starttidspunkt >= :dato")
    @RegisterRowMapper(HendelseMapper.class)
    List<Hendelse> finnHendelserEtter(@BindList("hendelsestyper") List<Hendelsestype> hendelsestyper, @Bind("dato") LocalDateTime dato);

    @SqlQuery("select id, sted, hendelsestype, starttidspunkt, paameldingstidspunkt, varighet_minutter, lenke, info, siste_slack_oppdatering, maks_antall from hendelse where hendelsestype in( <hendelsestyper> ) and starttidspunkt <= :dato")
    @RegisterRowMapper(HendelseMapper.class)
    List<Hendelse> finnHendelserFoer(@BindList("hendelsestyper") List<Hendelsestype> hendelsestyper, @Bind("dato") LocalDateTime dato);

    @SqlQuery("select id, sted, hendelsestype, starttidspunkt, paameldingstidspunkt, varighet_minutter, lenke, info, siste_slack_oppdatering, maks_antall from hendelse where hendelsestype = :hendelsestype and starttidspunkt >= :iDag order by starttidspunkt asc limit 1")
    @RegisterRowMapper(HendelseMapper.class)
    List<Hendelse> finnNesteHendelse(@Bind("hendelsestype") Hendelsestype hendelsestype, @Bind("iDag") LocalDateTime iDag);




}
