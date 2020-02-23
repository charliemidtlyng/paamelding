package no.charlie.client;


import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.Hendelsestype;
import no.charlie.domain.SlackPaths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackService {
    private final SlackClient slackClient;
    private final SlackPaths slackPaths;

    private static final Locale locale = new Locale("no", "NO");

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackService.class);

    public SlackService(SlackClient slackClient, SlackPaths slackPaths) {
        this.slackClient = slackClient;
        this.slackPaths = slackPaths;
    }

    public void oppdaterSlackKanalenMedHendelse(HendelseMedDeltakerinfo hendelseMedDeltakerinfo) {
        try {
            sendMelding(lagSlackMeldingForHendelse(hendelseMedDeltakerinfo), hendelseMedDeltakerinfo.getHendelseInfo().getHendelsestype());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke oppdatere slack med hendelse", e);
        }
    }

    public void oppdaterSlackKanalMedEndringAvPaameldte(Hendelse hendelse, List<Deltaker> deltakere) {
        try {
            String overskrift = String.format("Påmeldt ✅ %s på %s:", hendelse.getHendelsestype().toString().toLowerCase(), dagNavn(hendelse.getStarttid()));
            List<String> slackLinjer = deltakere.stream()
                    .map(deltaker -> String.format("<@%s>", deltaker.getSlacknavn().replace("@", ""), deltaker.getSlacknavn()))
                    .collect(Collectors.toList());
            slackLinjer.add(0, overskrift);

            sendMelding(new SlackMelding(slackLinjer), hendelse.getHendelsestype());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke oppdatere slack med hendelse", e);
        }
    }


    public void oppdaterSlackKanalMedEndringAvAvmeldte(Hendelse hendelse, List<Deltaker> deltakere) {
        try {
            String overskrift = String.format("Avmeldt 🤕 %s på %s:", hendelse.getHendelsestype().toString().toLowerCase(), dagNavn(hendelse.getStarttid()));
            List<String> slackLinjer = deltakere.stream()
                    .map(deltaker -> String.format("<@%s>", deltaker.getSlacknavn().replace("@", "")))
                    .collect(Collectors.toList());
            slackLinjer.add(0, overskrift);

            sendMelding(new SlackMelding(slackLinjer), hendelse.getHendelsestype());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke oppdatere slack med hendelse", e);
        }
    }


    private SlackMelding lagSlackMeldingForHendelse(HendelseMedDeltakerinfo hendelseMedDeltakerinfo) {
        Hendelse hendelse = hendelseMedDeltakerinfo.getHendelseInfo();
        String overskrift = String.format("%s i %s", hendelse.getHendelsestype(), hendelse.getSted());
        String lenkeTekst = String.format("<"+slackPaths.finnRiktigLenke(hendelse.getHendelsestype())+"%s|Meld deg på her>", hendelse.getId());
        String tekst = String.format("%s i %s - %s. \n Påmelding åpner %s",
                hendelse.getHendelsestype(),
                hendelse.getSted(),
                tilLesbartTidspunkt(hendelse.getStarttid()),
                tilLesbartTidspunkt(hendelse.getPaameldingstid())
        );

        int ledigePlasser = hendelse.getMaksAntallDeltakere() - hendelseMedDeltakerinfo.getAntallPaameldteDeltakere();
        String antallPaameldtTekst = hendelseMedDeltakerinfo.getAntallPaameldteDeltakere() > 0
                ? String.format("Så langt har %s meldt seg på. Det er %s ledige plasser",
                hendelseMedDeltakerinfo.getAntallPaameldteDeltakere(),
                ledigePlasser > 0 ? ledigePlasser : "ingen")
                : null;
        return new SlackMelding(overskrift, tekst, antallPaameldtTekst, lenkeTekst);
    }

    private void sendMelding(SlackMelding slackMelding, Hendelsestype hendelsestype) {
        String response = slackClient.postTilSlackkanal(slackPaths.finnRiktigKanal(hendelsestype), slackMelding);
        LOGGER.info(response);
    }

    public static String tilLesbartTidspunkt(LocalDateTime starttid) {
        return starttid.format(DateTimeFormatter.ofPattern("EEEE, dd. MMM - HH:MM", locale));

    }

    public static String dagNavn(LocalDateTime starttid) {
        return starttid.format(DateTimeFormatter.ofPattern("EEEE", locale));

    }

    public void oppdaterSlackKanalMedEndringAvDeltaker(String endringstekst, Hendelse hendelse, Deltaker deltaker) {
        try {
            String overskrift = String.format("%s %s %s på %s", deltaker.getSlacknavn(), endringstekst, hendelse.getHendelsestype().toString().toLowerCase(), dagNavn(hendelse.getStarttid()));
            sendMelding(new SlackMelding(overskrift), hendelse.getHendelsestype());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke oppdatere slack med hendelse", e);
        }
    }
}
