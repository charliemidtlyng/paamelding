package no.charlie.api;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.io.TimezoneInfo;
import biweekly.property.Summary;
import biweekly.util.Duration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.charlie.domain.DeltakerRequest;
import no.charlie.domain.Hendelse;
import no.charlie.domain.HendelseMedAntallDeltakere;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.HendelseRequest;
import no.charlie.domain.Hendelsestype;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static no.charlie.domain.Hendelsestype.*;

@Path("/ical")
@Produces(MediaType.TEXT_PLAIN)
@Api("Kalender-feed")
public class KalenderFeedResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(KalenderFeedResource.class);

    private final HendelseService hendelseService;

    public KalenderFeedResource(HendelseService hendelseService) {
        this.hendelseService = hendelseService;
    }

    @GET
    @Timed
    @Path("/fotball")
    @ApiOperation("Hent ut ical feed for hendelsestyper")
    @ApiResponses (@ApiResponse(code = 200, message = "Feed med hendelser", response = String.class))
    public Response fotballFeed() {
        ICalendar ical = byggIcalForHendelser(Arrays.asList(Fotballtrening, Fotballkamp));
        return Response.ok(Biweekly.write(ical).go()).build();
    }

    @GET
    @Timed
    @Path("/volleyball")
    @ApiOperation("Hent ut ical feed for hendelsestyper")
    @ApiResponses (@ApiResponse(code = 200, message = "Feed med hendelser", response = String.class))
    public Response volleyFeed() {
        ICalendar ical = byggIcalForHendelser(Arrays.asList(Volleyballtrening, Volleyballkamp));
        return Response.ok(Biweekly.write(ical).go()).build();
    }

    @GET
    @Timed
    @Path("/innebandy")
    @ApiOperation("Hent ut ical feed for hendelsestyper")
    @ApiResponses (@ApiResponse(code = 200, message = "Feed med hendelser", response = String.class))
    public Response innebandyFeed() {
        ICalendar ical = byggIcalForHendelser(Arrays.asList(Innebandytrening, Innebandykamp));
        return Response.ok(Biweekly.write(ical).go()).build();
    }

    private ICalendar byggIcalForHendelser(List<Hendelsestype> hendelsestyper) {
        List<HendelseMedAntallDeltakere> hendelser = hendelseService.finnNyeHendelser(hendelsestyper);
        ICalendar ical = new ICalendar();
        hendelser.stream()
                .map(this::mapIcalEvent)
                .forEach(ical::addEvent);

        ical.setProductId("-//BEKK Paamelding//iCal4j 1.0//EN");
        ical.getTimezoneInfo().setDefaultTimezone(timezoneAssignment());
        return ical;
    }

    private TimezoneAssignment timezoneAssignment() {
        TimeZone javaTz = TimeZone.getTimeZone("Europe/Oslo");
        return new TimezoneAssignment(javaTz, "Europe/Oslo");
    }

    private VEvent mapIcalEvent(HendelseMedAntallDeltakere hendelseMedAntallDeltakere) {
        VEvent event = new VEvent();
        Hendelse hendelse = hendelseMedAntallDeltakere.getHendelseInfo();
        event.setSummary(hendelse.getHendelsestype().toString() + " - " + hendelse.getSted());
        event.setDateStart(Date.from(hendelse.getStarttid().atZone(ZoneId.of("Europe/Oslo")).toInstant()));
        event.setDuration(new Duration.Builder().minutes(hendelse.getVarighet()).build());
        event.setLocation(hendelse.getSted());
        return event;
    }

}
