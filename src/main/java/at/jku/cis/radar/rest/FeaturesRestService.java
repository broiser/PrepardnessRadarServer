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
import at.jku.cis.radar.model.FeatureEvoluation;
import at.jku.cis.radar.service.FeatureEvoluationService;
import at.jku.cis.radar.transformer.BaseEntity2LongTransformer;
import at.jku.cis.radar.transformer.FeatureEvoluation2FeatureTransformer;

@Path("features")
@Produces(MediaType.APPLICATION_JSON)
public class FeaturesRestService implements RestService {

    @Inject
    private BaseEntity2LongTransformer baseEntity2LongTransformer;
    @Inject
    private FeatureEvoluation2FeatureTransformer featureEvoluation2FeatureTransformer;
    @Inject
    private FeatureEvoluationService featureEvoluationService;

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
        List<FeatureEvoluation> featureEvoluations = featureEvoluationService.findBetween(eventId, fromDate, toDate);
        return Response.ok(buildFeatureCollection(featureEvoluations)).build();
    }

    @PUT
    @Path("{eventId}")
    public Response updateFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvoluation featureEvoluation = featureEvoluationService.update(eventId, feature);
        return Response.ok(featureEvoluation.getId()).build();
    }

    @POST
    @Path("{eventId}")
    public Response saveFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvoluation featureEvoluation = featureEvoluationService.save(eventId, feature);
        return Response.ok(featureEvoluation.getId()).build();
    }

    @POST
    @Path("{eventId}")
    public Response saveFeatures(@PathParam("eventId") long eventId, FeatureCollection featureCollection) {
        List<FeatureEvoluation> featureEvoluations = featureEvoluationService.save(eventId,
                featureCollection.getFeatures());
        return Response.ok(CollectionUtils.collect(featureEvoluations, baseEntity2LongTransformer)).build();
    }

    private FeatureCollection buildFeatureCollection(List<FeatureEvoluation> featureEvoluations) {
        return new FeatureCollection(CollectionUtils.collect(featureEvoluations, featureEvoluation2FeatureTransformer));
    }
}
