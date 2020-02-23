package no.charlie.api;

import no.charlie.db.DeltakerDAO;
import no.charlie.db.EndringDAO;
import no.charlie.domain.Deltaker;
import no.charlie.domain.DeltakerRequest;
import no.charlie.domain.HendelseMedDeltakerinfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.codahale.metrics.annotation.Timed;

public class DeltakerService {

    private final DeltakerDAO deltakerDAO;
    private final EndringDAO endringDAO;
    private final HendelseService hendelseService;

    public DeltakerService(DeltakerDAO deltakerDAO, EndringDAO endringDAO, HendelseService hendelseService) {
        this.deltakerDAO = deltakerDAO;
        this.endringDAO = endringDAO;
        this.hendelseService = hendelseService;
    }

    @Timed
    public void meldPaaHendelse(int hendelseId, DeltakerRequest deltakerRequest) {
        int deltakerId = deltakerDAO.meldPaa(deltakerRequest.getNavn(),
                deltakerRequest.getSlacknavn(),
                LocalDateTime.now(),
                null,
                hendelseId
        );
        List<Integer> nyeDeltakere = oppdaterUttakEtterEndring(hendelseId);
        if (nyeDeltakere.isEmpty()) {
            endringDAO.opprettEndring(hendelseId, deltakerId, "er på reservelisten til", LocalDateTime.now());
        }
    }


    public void meldAvHendelse(int hendelseId, int deltakerId) {
        deltakerDAO.meldAv(deltakerId, LocalDateTime.now());
        List<Integer> nyeDeltakere = oppdaterUttakEtterEndring(hendelseId);
        if (!nyeDeltakere.isEmpty()) {
            endringDAO.opprettEndring(hendelseId, deltakerId, "er avmeldt", LocalDateTime.now());
        }
    }

    private List<Integer> oppdaterUttakEtterEndring(int hendelseId) {
        return hendelseService.finnHendelseMedDeltakerInfo(hendelseId)
                .map(this::gjennomfoerUttak)
                .orElse(Collections.emptyList());
    }

    private List<Integer> gjennomfoerUttak(HendelseMedDeltakerinfo hendelse) {
        if (hendelse.getHendelseInfo().getHendelsestype().erTrening()) {
            List<Deltaker> uttatteDeltakere = nyeUttatteDeltakere(hendelse);
            return oppdaterUttak(uttatteDeltakere, hendelse.getHendelseInfo().getId());
        }
        return Collections.emptyList();
    }

    private List<Integer> oppdaterUttak(List<Deltaker> uttatteDeltakere, int hendelseId) {
        List<Integer> deltakerIder = uttatteDeltakere.stream()
                .map(deltaker -> {
                    endringDAO.opprettEndring(hendelseId, deltaker.getId(), "er påmeldt til", LocalDateTime.now());
                    return deltaker.getId();
                })
                .collect(Collectors.toList());
        if (!deltakerIder.isEmpty()) {
            deltakerDAO.settUttatt(deltakerIder);
        }
        return deltakerIder;
    }

    private List<Deltaker> nyeUttatteDeltakere(HendelseMedDeltakerinfo hendelse) {
        return hendelse.getDeltakere().stream()
                .filter(deltaker -> deltaker.getAvmeldingstidspunkt() == null)
                .sorted(this::deltakerSortering)
                .limit(hendelse.getHendelseInfo().getMaksAntallDeltakere())
                .filter(Deltaker::erIkkeUttatt)
                .collect(Collectors.toList());
    }

    private int deltakerSortering(Deltaker o1, Deltaker o2) {
        if (o1.getRegistreringstidspunkt().isBefore(o2.getRegistreringstidspunkt())) {
            return -1;
        }
        if (o1.getRegistreringstidspunkt().isAfter(o2.getRegistreringstidspunkt())) {
            return 1;
        }

        return 0;
    }

}
