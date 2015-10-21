package at.jku.cis.radar.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureCollection;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvoluationService;
import at.jku.cis.radar.transformer.FeatureEvoluation2FeatureTransformer;
import at.jku.cis.radar.transformer.FeatureEvoluation2GroupTransformer;

@Path("features")
public class FeatureRestService extends RestService {

    @Inject
    private FeatureEvoluationService featureEvoluationService;
    @Inject
    private FeatureEvoluation2FeatureTransformer featureEvoluation2FeatureTransformer;
    @Inject
    private FeatureEvoluation2GroupTransformer featureEvoluation2GroupTransformer;

    @GET
    @Path("{eventId}")
    public Response getFeatures(@PathParam("eventId") long eventId) {
        long currentMillis = DateTime.now().getMillis();
        return getFeatures(eventId, currentMillis, currentMillis);
    }

    @GET
    @Path("{eventId}/{from}/{to}")
    public Response getFeatures(@PathParam("eventId") long eventId, @PathParam("from") long from,
            @PathParam("to") long to) {
        DateTime toDate = new DateTime(to);
        DateTime fromDate = new DateTime(from);
        List<FeatureEvolution> featureEvoluations = featureEvoluationService.findBetween(eventId, fromDate, toDate);
        return Response.ok(buildFeatureCollection(featureEvoluations)).build();
    }

    @PUT
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvolution featureEvoluation = featureEvoluationService.update(eventId, feature);
        return Response.ok(featureEvoluation2GroupTransformer.transform(featureEvoluation)).build();
    }

    @POST
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvolution featureEvoluation = featureEvoluationService.save(eventId, feature);
        return Response.ok(featureEvoluation2GroupTransformer.transform(featureEvoluation)).build();
    }

    private FeatureCollection buildFeatureCollection(List<FeatureEvolution> featureEvoluations) {
        return new FeatureCollection(CollectionUtils.collect(featureEvoluations, featureEvoluation2FeatureTransformer));
    }
}
