package no.charlie.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.charlie.client.NifClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/nif")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("Nif kamper")
public class NifKampResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NifKampResource.class);

    private final NifClient nifClient;

    public NifKampResource(NifClient nifClient) {
        this.nifClient = nifClient;
    }

    @GET
    @Timed
    @Path("/fotball")
    @ApiOperation("Hent ut kamper for fotball")
    @ApiResponses(@ApiResponse(code = 200, message = "Feed med hendelser", response = String.class))
    public Response fotball() {

        return Response.ok(nifClient.hentFotballKamper("01.10.2019", "01.05.2020")).build();
    }

    @GET
    @Timed
    @Path("/volleyball")
    @ApiOperation("Hent ut kamper for volleyball")
    @ApiResponses(@ApiResponse(code = 200, message = "Feed med hendelser", response = String.class))
    public Response volley() {
        return Response.ok(nifClient.hentVolleyballKamper("01.10.2019", "01.05.2020")).build();
    }

}
