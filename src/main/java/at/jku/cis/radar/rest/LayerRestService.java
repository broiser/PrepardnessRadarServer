package at.jku.cis.radar.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import at.jku.cis.radar.transformer.FeatureTransformer;

@Path("layers")
@Produces(MediaType.APPLICATION_JSON)
public class LayerRestService implements RestService {

    @Inject
    private FeatureTransformer featureTransformer;
    @Inject
    private FeatureEvoluationService featureEvoluationService;

    @GET
    @Path("{eventId}")
    public Response getLayer(@PathParam("eventId") long eventId) {
        FeatureEvoluation featureEvoluation = featureEvoluationService.findLatest(eventId);
        return Response.ok(new FeatureCollection(featureEvoluation.getFeature())).build();
    }

    @GET
    @Path("{eventId}/{from}/{to}")
    public Response getLayer(@PathParam("eventId") long eventId, @PathParam("from") long from,
            @PathParam("to") long to) {
        DateTime toDate = new DateTime(to);
        DateTime fromDate = new DateTime(from);
        List<FeatureEvoluation> featureEvoluations = featureEvoluationService.findBetween(eventId, fromDate, toDate);
        return Response.ok(new FeatureCollection(collectFeatures(featureEvoluations))).build();
    }

    @POST
    @Path("{eventId}")
    public Response save(@PathParam("eventId") long eventId, FeatureCollection featureCollection) {
        featureEvoluationService.save(eventId, featureCollection.getFeatures());
        return Response.ok().build();
    }

    private List<Feature> collectFeatures(List<FeatureEvoluation> featureEvoluations) {
        return new ArrayList<Feature>(CollectionUtils.collect(featureEvoluations, featureTransformer));
    }
}
