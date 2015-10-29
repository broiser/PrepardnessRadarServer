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

import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.geojson.FeatureCollection;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvolutionService;
import at.jku.cis.radar.transformer.FeatureEvolution2FeatureTransformer;
import at.jku.cis.radar.transformer.FeatureEvolution2GroupTransformer;

@Path("features")
public class FeatureRestService extends RestService {

    @Inject
    private FeatureEvolutionService featureEvolutionService;
    @Inject
    private FeatureEvolution2FeatureTransformer featureEvolution2FeatureTransformer;
    @Inject
    private FeatureEvolution2GroupTransformer featureEvolution2GroupTransformer;

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
        List<FeatureEvolution> featureEvolutions = featureEvolutionService.findBetween(eventId, fromDate, toDate);
        return Response.ok(buildFeatureCollection(featureEvolutions)).build();
    }

    @PUT
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvolution featureEvolution = featureEvolutionService.save(eventId, feature);
        return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
    }

    @POST
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createFeature(@PathParam("eventId") long eventId, Feature feature) {
        FeatureEvolution featureEvolution = featureEvolutionService.create(eventId, feature);
        return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
    }

    private FeatureCollection buildFeatureCollection(List<FeatureEvolution> featureEvolutions) {
        return new FeatureCollection(CollectionUtils.collect(featureEvolutions, featureEvolution2FeatureTransformer));
    }
}
