package no.charlie.api;

import no.charlie.db.DeltakerDAO;
import no.charlie.db.HendelseDAO;
import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.Hendelsestype;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HendelseService {

    private final HendelseDAO hendelseDAO;
    private final DeltakerDAO deltakerDAO;

    public HendelseService(HendelseDAO hendelseDAO, DeltakerDAO deltakerDAO) {
        this.hendelseDAO = hendelseDAO;
        this.deltakerDAO = deltakerDAO;
    }


    public List<HendelseMedDeltakerinfo> finnNyeHendelser(List<Hendelsestype> hendelsestyper) {
        List<Hendelse> hendelser = hendelseDAO.finnHendelserEtter(hendelsestyper, LocalDateTime.now());
        Map<Integer, Integer> antallMap = hendelser.isEmpty() ? new HashMap<>() : deltakerDAO.finnDeltakerAntallForHendelser(hendelsesIder(hendelser));
        return hendelser.stream()
                .map(hendelse -> new HendelseMedDeltakerinfo()
                        .withHendelseInfo(hendelse)
                        .withAntallPaameldteDeltakere(antallMap.getOrDefault(hendelse.getId(), 0)))
                .collect(Collectors.toList());
    }

    private List<Integer> hendelsesIder(List<Hendelse> hendelser) {
        return hendelser.stream()
        .map(Hendelse::getId)
        .collect(Collectors.toList());
    }


    public Optional<HendelseMedDeltakerinfo> finnHendelseMedDeltakerInfo(int id) {
        Optional<Hendelse> optHendelse = hendelseDAO.finnHendelse(id);
        return optHendelse.map( hendelse -> {
            List<Deltaker> deltakere = deltakerDAO.finnDeltakereForHendelse(hendelse.getId());
            return new HendelseMedDeltakerinfo().withHendelseInfo(hendelse)
                    .withDeltakere(deltakere)
                    .withAntallPaameldteDeltakere((int) deltakere.stream().filter(Deltaker::erPaameldt).count());
        });
    }

    public Optional<Hendelse> finnHendelse(int id) {
        return hendelseDAO.finnHendelse(id);
    }

    public HendelseMedDeltakerinfo opprettHendelse(Hendelse hendelse) {
        int id = hendelseDAO.leggTilHendelse(
                hendelse.getSted(),
                hendelse.getHendelsestype(),
                hendelse.getStarttid(),
                hendelse.getPaameldingstid(),
                hendelse.getVarighet(),
                hendelse.getMaksAntallDeltakere(),
                hendelse.getLenke(),
                hendelse.getInfo()
        );

        return finnHendelseMedDeltakerInfo(id)
                .orElseThrow(() -> new RuntimeException("Noe galt skjedde med opprettelse av ny hendelse"));
    }
}
