package no.charlie.jobs;

import no.charlie.api.HendelseService;
import no.charlie.client.SlackService;
import no.charlie.db.EndringDAO;
import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.knowm.sundial.Job;
import org.knowm.sundial.SundialJobScheduler;
import org.knowm.sundial.annotations.SimpleTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SimpleTrigger(repeatInterval = 15, timeUnit = TimeUnit.MINUTES, isConcurrencyAllowed = false)
public class OppdaterSlackMedDeltakerEndringerJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(OppdaterSlackMedDeltakerEndringerJob.class);
    private HendelseService hendelseService;
    private SlackService slackService;
    private EndringDAO endringDAO;


    public void setup() {
        this.hendelseService = (HendelseService) SundialJobScheduler.getServletContext().getAttribute("hendelseService");
        this.slackService = (SlackService) SundialJobScheduler.getServletContext().getAttribute("slackService");
        this.endringDAO = (EndringDAO) SundialJobScheduler.getServletContext().getAttribute("endringDAO");
    }


    @Override
    public void doRun() throws JobInterruptException {
        LOGGER.info("Starter jobb");
        setup();

        endringDAO.finnNyeEndringer().forEach(endring ->
                hendelseService.finnHendelseMedDeltakerInfo(endring.getHendelseid())
                        .ifPresent(hendelse -> hendelse.getDeltakere()
                                .stream()
                                .filter(deltaker -> deltaker.getId() == endring.getDeltakerId())
                                .forEach(deltaker -> {
                                    slackService.oppdaterSlackKanalMedEndringAvDeltaker(endring.getEndringstekst(), hendelse.getHendelseInfo(), deltaker);
                                    endringDAO.settOppdatert(endring.getId());
                                })
                        )
        );
        LOGGER.info("Jobb ferdig");

    }

    private List<Deltaker> finnAvmeldtSiden(List<Deltaker> deltakere, LocalDateTime siden) {
        return siden == null
                ? deltakere.stream().filter(deltaker -> deltaker.getAvmeldingstidspunkt() != null).collect(Collectors.toList())
                : deltakere.stream().filter(deltaker -> deltaker.getAvmeldingstidspunkt().isAfter(siden)).collect(Collectors.toList());
    }

    private List<Deltaker> finnPaameldtSiden(List<Deltaker> deltakere, LocalDateTime siden) {
        return siden == null
                ? deltakere
                : deltakere.stream()
                .filter(deltaker -> deltaker.getRegistreringstidspunkt().isAfter(siden))
                .collect(Collectors.toList());
    }

    private void oppdaterAvmeldte(Hendelse hendelse, List<Deltaker> deltakere) {
        if (!deltakere.isEmpty()) {
            slackService.oppdaterSlackKanalMedEndringAvAvmeldte(hendelse, deltakere);
        }
    }

    private void oppdaterPaameldte(Hendelse hendelse, List<Deltaker> deltakere) {
        if (!deltakere.isEmpty()) {
            slackService.oppdaterSlackKanalMedEndringAvPaameldte(hendelse, deltakere);
        }
    }
}
