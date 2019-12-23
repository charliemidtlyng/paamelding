package no.charlie.client;


import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.Hendelsestype;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackService {
    private final SlackClient slackClient;
    private final String fotballPath;
    private final String innebandyPath;
    private final String volleyballPath;
    private final String charliePath = "T028UJTLQ/BCQ56FMPX/8M2mzs3yD8YOeT1FcFrrMLUf";

    private static final Locale locale = new Locale("no", "NO");

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackService.class);

    public SlackService(SlackClient slackClient, String fotballPath, String innebandyPath, String volleyballPath) {
        this.slackClient = slackClient;
        this.fotballPath = fotballPath;
        this.volleyballPath = volleyballPath;
        this.innebandyPath = innebandyPath;
    }

    public void oppdaterSlackKanalenMedHendelse(HendelseMedDeltakerinfo hendelseMedDeltakerinfo) {
        try {
            sendMelding(lagSlackMeldingForHendelse(hendelseMedDeltakerinfo), hendelseMedDeltakerinfo.getHendelseInfo().getHendelsestype());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke oppdatere slack med hendelse", e);
        }
    }

    public void oppdaterSlackKanalMedEndringAvPaameldte(HendelseMedDeltakerinfo hendelseMedDeltakerinfo, List<Deltaker> nyeDeltakere) {
        try {
            Hendelse hendelse = hendelseMedDeltakerinfo.getHendelseInfo();
            String overskrift = String.format("Påmeldt til %s på %s:", hendelse.getHendelsestype(), dagNavn(hendelse.getStarttid()));
            List<String> slackLinjer = nyeDeltakere.stream()
                    .map(deltaker -> String.format("<%s|%s>", deltaker.getSlacknavn(), deltaker.getSlacknavn()))
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
        String lenkeTekst = String.format("<https://fotball.bekk.no/%s|Meld deg på her>", hendelse.getId());
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
                ledigePlasser>0 ? ledigePlasser : "ingen")
                : null;
        return new SlackMelding(overskrift, tekst, antallPaameldtTekst, lenkeTekst);
    }

    private void sendMelding(SlackMelding slackMelding, Hendelsestype hendelsestype) {
        String response = slackClient.postTilSlackkanal(velgKanal(hendelsestype), slackMelding);
        LOGGER.info(response);
    }

    private String velgKanal(Hendelsestype hendelsestype) {
        return charliePath;
//        switch (hendelsestype) {
//            case Volleyballkamp:
//            case Volleyballtrening:
//                return volleyballPath;
//            case Fotballkamp:
//            case Fotballtrening:
//                return fotballPath;
//            case Innebandykamp:
//            case Innebandytrening:
//                return innebandyPath;
//            default:
//                throw new RuntimeException("Ukjent hendelsestype");
//        }

    }

    public static String tilLesbartTidspunkt(LocalDateTime starttid) {
        return starttid.format(DateTimeFormatter.ofPattern("EEEE, dd. MMM - HH:MM", locale));

    }

    public static String dagNavn(LocalDateTime starttid) {
        return starttid.format(DateTimeFormatter.ofPattern("EEEE", locale));

    }

}
