package at.jku.cis.radar.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.dao.EventDao;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventRestService implements RestService {

    @Inject
    private EventDao eventDao;

    @GET
    public Response getEvents() {
        return Response.ok(eventDao.findAll()).build();
    }

}
