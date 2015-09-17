package at.jku.cis.radar.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.FeatureCollection;

@Path("layers")
@Produces(MediaType.APPLICATION_JSON)
public class LayerRestService implements RestService {

    @Inject
    private EventDao eventDao;

    @GET
    @Path("{eventId}")
    public Response getLayer(@PathParam("eventId") long eventId) {
        Event event = eventDao.findById(eventId);
        return Response.ok(new FeatureCollection()).build();
    }

    @POST
    @Path("{eventId}")
    public Response save(@PathParam("eventId") long eventId, FeatureCollection featureCollection) {
        Event event = eventDao.findById(eventId);

        return Response.ok().build();
    }
}
