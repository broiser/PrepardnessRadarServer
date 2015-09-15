package at.jku.cis.radar.rest;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("layers")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class LayerRestService implements Serializable {

    @GET
    @Path("{eventName}")
    public Response getLayer(@PathParam("eventName") String eventName) {
        return Response.ok().build();
    }
}
