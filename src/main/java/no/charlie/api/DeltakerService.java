package no.charlie.api;

import no.charlie.db.DeltakerDAO;
import no.charlie.domain.Deltaker;
import no.charlie.domain.DeltakerRequest;
import no.charlie.domain.HendelseMedDeltakerinfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DeltakerService {

    private final DeltakerDAO deltakerDAO;
    private final HendelseService hendelseService;

    public DeltakerService(DeltakerDAO deltakerDAO, HendelseService hendelseService) {
        this.deltakerDAO = deltakerDAO;
        this.hendelseService = hendelseService;
    }


    public void meldPaaHendelse(int hendelseId, DeltakerRequest deltakerRequest) {
        deltakerDAO.meldPaa(deltakerRequest.getNavn(),
                deltakerRequest.getSlacknavn(),
                LocalDateTime.now(),
                null,
                hendelseId
        );
        oppdaterUttakEtterEndring(hendelseId);

    }


    public void meldAvHendelse(int hendelseId, int deltakerId) {
        deltakerDAO.meldAv(deltakerId, LocalDateTime.now());
        oppdaterUttakEtterEndring(hendelseId);
    }

    private void oppdaterUttakEtterEndring(int hendelseId) {
        hendelseService.finnHendelseMedDeltakerInfo(hendelseId)
                .ifPresent(this::gjennomfoerUttak);
    }

    private void gjennomfoerUttak(HendelseMedDeltakerinfo hendelse) {
        if (hendelse.getHendelseInfo().getHendelsestype().harAutomatiskUttak()) {
            List<Deltaker> uttatteDeltakere = nyeUttatteDeltakere(hendelse);
            oppdaterUttak(uttatteDeltakere);
        }
    }

    private void oppdaterUttak(List<Deltaker> uttatteDeltakere) {
        List<Integer> deltakerIder = uttatteDeltakere.stream().map(Deltaker::getId).collect(Collectors.toList());
        if(!deltakerIder.isEmpty()) {
            deltakerDAO.settUttatt(deltakerIder);
        }
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
        if (o1.getRegistreringstidspunkt().isBefore(o2.getRegistreringstidspunkt())){
            return -1;
        }
        if (o1.getRegistreringstidspunkt().isAfter(o2.getRegistreringstidspunkt())) {
            return 1;
        }

        return 0;
    }

}
