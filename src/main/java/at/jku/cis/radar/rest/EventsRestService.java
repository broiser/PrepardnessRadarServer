package at.jku.cis.radar.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.service.EventService;

@Path("events")
public class EventsRestService extends RestService {

    @Inject
    private EventService eventService;

    @GET
    public Response getEvents() {
        return Response.ok(eventService.findAll()).build();
    }
}
