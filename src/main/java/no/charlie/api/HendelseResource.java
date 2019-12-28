package no.charlie.api;

import io.swagger.annotations.Api;
import no.charlie.domain.Deltaker;
import no.charlie.domain.Hendelse;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.Hendelsestype;

import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hendelse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("Hendelse")
public class HendelseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HendelseResource.class);

    private final HendelseService hendelseService;
    private final DeltakerService deltakerService;
    private final Validator validator;

    public HendelseResource(HendelseService hendelseService, DeltakerService deltakerService, Validator validator) {
        this.hendelseService = hendelseService;
        this.deltakerService = deltakerService;
        this.validator = validator;
    }

    @GET
    @Timed
    @Path("/{id}")
    public Response finnHendelse(@PathParam("id") int id) {
        Optional<HendelseMedDeltakerinfo> optHendelse = hendelseService.finnHendelseMedDeltakerInfo(id);
        return optHendelse.map(hendelse -> Response.ok(hendelse).build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Timed
    @Path("/nye")
    public Response finnNyeHendelse(@QueryParam("typer") List<Hendelsestype> hendelsestyper) {
        if (hendelsestyper.isEmpty()) {
            return Response.status(400).entity("Mangler hendelsestyper").build();
        }
        return Response.ok(hendelseService.finnNyeHendelser(hendelsestyper)).build();
    }

    @POST
    @Timed
    @Path("/opprett")
    public Response opprettHendelse(Hendelse hendelse) {
        validator.sjekkUgyldigeVerdierForHendelse(hendelse);
        try {
            HendelseMedDeltakerinfo opprettet = hendelseService.opprettHendelse(hendelse);
            return Response.ok(opprettet).build();
        } catch (Exception e) {
            LOGGER.error("Kunne ikke opprette hendelse", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Timed
    @Path("/meld-paa/{hendelseId}")
    public Response meldPaaHendelse(@PathParam("hendelseId") int hendelseId, @Context HttpHeaders httpHeaders, Deltaker deltaker) {
        validator.sjekkUgyldigeVerdierForPaamelding(hendelseService.finnHendelse(hendelseId).isPresent(), httpHeaders);
        try {
            deltakerService.meldPaaHendelse(hendelseId, deltaker);
            return Response.accepted().build();
        } catch (Exception e) {
            LOGGER.error("Kunne ikke melde på hendelse", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Timed
    @Path("/meld-av/{hendelseId}/{deltakerId}")
    public Response meldAvHendelse(@PathParam("hendelseId") int hendelseId, @PathParam("deltakerId") int deltakerId) {
        validator.sjekkUgyldigeVerdierForAvmelding(deltakerId, hendelseService.finnHendelse(hendelseId));
        try {
            deltakerService.meldAvHendelse(hendelseId, deltakerId);
            return Response.accepted().build();
        } catch (Exception e) {
            LOGGER.error("Kunne ikke opprette hendelse", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}
