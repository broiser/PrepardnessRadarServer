package at.jku.cis.radar.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.model.FeatureCollection;
import at.jku.cis.radar.model.FeatureEvoluation;
import at.jku.cis.radar.service.FeatureEvoluationService;

@Path("layers")
@Produces(MediaType.APPLICATION_JSON)
public class LayerRestService implements RestService {

    @Inject
    private FeatureEvoluationService featureEvoluationService;

    @GET
    @Path("{eventId}")
    public Response getLayer(@PathParam("eventId") long eventId) {
        FeatureEvoluation featureEvoluation = featureEvoluationService.findLatest(eventId);
        return Response.ok(new FeatureCollection(featureEvoluation.getFeature())).build();
    }

    @POST
    @Path("{eventId}")
    public Response save(@PathParam("eventId") long eventId, FeatureCollection featureCollection) {
        featureEvoluationService.save(eventId, featureCollection.getFeatures());
        return Response.ok().build();
    }
}
