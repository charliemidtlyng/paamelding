package no.charlie.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.charlie.domain.DeltakerRequest;
import no.charlie.domain.HendelseMedAntallDeltakere;
import no.charlie.domain.HendelseMedDeltakerinfo;
import no.charlie.domain.HendelseRequest;
import no.charlie.domain.Hendelsestype;

import java.util.List;
import java.util.Optional;
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
    @ApiOperation("Hent ut informasjon om en enkelt hendelse")
    @ApiResponses (@ApiResponse(code = 200, message = "En enkelt hendelse", response = HendelseMedDeltakerinfo.class))
    public Response finnHendelse(@PathParam("id") int id) {
        Optional<HendelseMedDeltakerinfo> optHendelse = hendelseService.finnHendelseMedDeltakerInfo(id);
        return optHendelse.map(hendelse -> Response.ok(hendelse).build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Timed
    @Path("/nye")
    @ApiOperation("Hent ut en liste over hendelser frem i tid")
    @ApiResponses (@ApiResponse(code = 200, message = "Liste med hendelser", response = HendelseMedAntallDeltakere.class, responseContainer = "List"))
    public Response finnNyeHendelser(@QueryParam("typer") List<Hendelsestype> hendelsestyper) {
        if (hendelsestyper.isEmpty()) {
            return Response.status(400).entity("Mangler hendelsestyper").build();
        }
        return Response.ok(hendelseService.finnNyeHendelser(hendelsestyper)).build();
    }

    @GET
    @Timed
    @Path("/historiske")
    @ApiOperation("Hent ut en liste over historiske hendelser")
    @ApiResponses (@ApiResponse(code = 200, message = "Liste med hendelser", response = HendelseMedAntallDeltakere.class, responseContainer = "List"))
    public Response finnHistoriskeHendelser(@QueryParam("typer") List<Hendelsestype> hendelsestyper) {
        if (hendelsestyper.isEmpty()) {
            return Response.status(400).entity("Mangler hendelsestyper").build();
        }
        return Response.ok(hendelseService.finnHistoriskeHendelser(hendelsestyper)).build();
    }

    @POST
    @Timed
    @Path("/opprett")
    @ApiOperation("Opprett en ny hendelse")
    @ApiResponses (@ApiResponse(code = 200, message = "Den opprettede hendelsen", response = HendelseMedDeltakerinfo.class))
    public Response opprettHendelse(HendelseRequest hendelse) {
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
    @ApiOperation(value = "Melde deltaker på hendelse")
    @ApiResponses (@ApiResponse(code = 200, message = "Den påmeldte hendelsen", response = HendelseMedDeltakerinfo.class))
    public Response meldPaaHendelse(@PathParam("hendelseId") int hendelseId, @HeaderParam ("X-captcha-request") String captcha, @Context HttpHeaders httpHeaders, DeltakerRequest deltakerRequest) {
        validator.sjekkUgyldigeVerdierForPaamelding(hendelseService.finnHendelse(hendelseId).isPresent(), httpHeaders);
        try {
            deltakerService.meldPaaHendelse(hendelseId, deltakerRequest);
            return Response.ok(hendelseService.finnHendelseMedDeltakerInfo(hendelseId).get()).build();
        } catch (Exception e) {
            LOGGER.error("Kunne ikke melde på hendelse", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Timed
    @Path("/meld-av/{hendelseId}/{deltakerId}")
    @ApiOperation("Melde deltaker av hendelse")
    @ApiResponses (@ApiResponse(code = 200, message = "Den avmeldte hendelsen", response = HendelseMedDeltakerinfo.class))
    public Response meldAvHendelse(@PathParam("hendelseId") int hendelseId, @PathParam("deltakerId") int deltakerId) {
        validator.sjekkUgyldigeVerdierForAvmelding(deltakerId, hendelseService.finnHendelse(hendelseId));
        try {
            deltakerService.meldAvHendelse(hendelseId, deltakerId);
            return Response.ok(hendelseService.finnHendelseMedDeltakerInfo(hendelseId).get()).build();
        } catch (Exception e) {
            LOGGER.error("Kunne ikke opprette hendelse", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GET
    @Path("/typer")
    @ApiOperation("Hent ut en liste over gyldige hendelsestyper")
    @ApiResponses (@ApiResponse(code = 200, message = "Tilgjengelige hendelsestyper", response = Hendelsestype.class, responseContainer = "List"))
    public Response gyldigeHendelsestyper() {
        return Response.ok(Hendelsestype.values()).build();
    }

}
