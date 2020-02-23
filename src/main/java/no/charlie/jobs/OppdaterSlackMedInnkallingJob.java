package no.charlie.jobs;

import no.charlie.api.HendelseService;
import no.charlie.client.SlackService;

import java.time.LocalDateTime;

import org.knowm.sundial.Job;
import org.knowm.sundial.SundialJobScheduler;
import org.knowm.sundial.annotations.CronTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CronTrigger(cron = "0 15 10 * * ?")
public class OppdaterSlackMedInnkallingJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(OppdaterSlackMedInnkallingJob.class);
    private HendelseService hendelseService;
    private SlackService slackService;


    public void setup() {
        this.hendelseService = (HendelseService) SundialJobScheduler.getServletContext().getAttribute("hendelseService");
        this.slackService = (SlackService) SundialJobScheduler.getServletContext().getAttribute("slackService");
    }


    @Override
    public void doRun() throws JobInterruptException {
        LOGGER.info("Starter jobb - OppdaterSlackMedInnkallingJob");
        setup();
        hendelseService.finnHendelserForSlackOppdatering()
                .forEach(hendelse -> {
                    slackService.oppdaterSlackKanalenMedHendelse(hendelse);
                    hendelseService.settSisteSlackOppdatering(hendelse.getHendelseInfo().getId(), LocalDateTime.now());
                });

        LOGGER.info("Jobb ferdig - OppdaterSlackMedInnkallingJob");

    }


}
