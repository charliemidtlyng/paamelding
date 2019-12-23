package no.charlie.api;

import no.charlie.client.SlackService;
import no.charlie.domain.HendelseMedDeltakerinfo;

import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/slack")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SlackResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackResource.class);

    private final SlackService slackService;
    private final HendelseService hendelseService;
    private final String magicHeader;

    public SlackResource(SlackService slackService, HendelseService hendelseService, String magicHeader) {
        this.slackService = slackService;
        this.hendelseService = hendelseService;
        this.magicHeader = magicHeader;
    }

    @POST
    @Timed
    @Path("/hendelse/{id}")
    public Response sendSlackMeldingForHendelse(@PathParam("id") int id, @HeaderParam("MagicHeader") String magicHeaderInput) {
        if(!magicHeader.equalsIgnoreCase(magicHeaderInput)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Optional<HendelseMedDeltakerinfo> hendelse = hendelseService.finnHendelseMedDeltakerInfo(id);
        slackService.oppdaterSlackKanalenMedHendelse(hendelse.get());
        return Response.noContent().build();
    }


}
