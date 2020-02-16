package no.charlie.api;

import no.charlie.client.SlackService;
import no.charlie.db.DeltakerDAO;
import no.charlie.domain.Deltaker;
import no.charlie.domain.DeltakerRequest;
import no.charlie.domain.HendelseMedDeltakerinfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeltakerService {

    private final DeltakerDAO deltakerDAO;
    private final HendelseService hendelseService;
    private final SlackService slackService;

    public DeltakerService(DeltakerDAO deltakerDAO, HendelseService hendelseService, SlackService slackService) {
        this.deltakerDAO = deltakerDAO;
        this.hendelseService = hendelseService;
        this.slackService = slackService;
    }


    public int meldPaaHendelse(int hendelseId, DeltakerRequest deltakerRequest) {
        deltakerDAO.meldPaa(deltakerRequest.getNavn(),
                deltakerRequest.getSlacknavn(),
                LocalDateTime.now(),
                null,
                hendelseId
        );
        oppdaterUttakEtterEndring(hendelseId);

        return hendelseId;
    }


    public void meldAvHendelse(int hendelseId, int deltakerId) {
        deltakerDAO.meldAv(deltakerId, LocalDateTime.now());
        oppdaterUttakEtterEndring(hendelseId);
    }

    private void oppdaterUttakEtterEndring(int hendelseId) {
        hendelseService.finnHendelseMedDeltakerInfo(hendelseId)
        .ifPresent(hendelse -> {
            List<Deltaker> nyeDeltakere = gjennomfoerUttak(hendelse);
            oppdaterSlack(hendelse, nyeDeltakere);
        });
    }

    private void oppdaterSlack(HendelseMedDeltakerinfo hendelse, List<Deltaker> nyeDeltakere) {
        if (!nyeDeltakere.isEmpty()) {
            slackService.oppdaterSlackKanalMedEndringAvPaameldte(hendelse, nyeDeltakere);
        }

    }

    private List<Deltaker> gjennomfoerUttak(HendelseMedDeltakerinfo hendelse) {
        if (hendelse.getHendelseInfo().getHendelsestype().harAutomatiskUttak()) {
            List<Deltaker> uttatteDeltakere = nyeUttatteDeltakere(hendelse);
            oppdaterUttak(uttatteDeltakere);
            return uttatteDeltakere;
        }
        return Collections.emptyList();
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
