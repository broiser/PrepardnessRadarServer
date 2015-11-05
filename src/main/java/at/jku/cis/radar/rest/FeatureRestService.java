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

import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvolutionService;
import at.jku.cis.radar.transformer.FeatureEvolution2GeoJsonFeatureTransformer;
import at.jku.cis.radar.transformer.FeatureEvolution2GroupTransformer;

@Path("features")
public class FeatureRestService extends RestService {

    @Inject
    private FeatureEvolutionService featureEvolutionService;
    @Inject
    private FeatureEvolution2GeoJsonFeatureTransformer featureEvolution2GeoJsonFeatureTransformer;
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
        List<FeatureEvolution> featureEvolutions = featureEvolutionService.findNewestByEvent(eventId, fromDate, toDate);
        return Response.ok(buildGeoJsonFeatureCollection(featureEvolutions)).build();
    }

    @PUT
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        FeatureEvolution featureEvolution = featureEvolutionService.save(eventId, geoJsonFeature);
        return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
    }

    @POST
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        FeatureEvolution featureEvolution = featureEvolutionService.create(eventId, geoJsonFeature);
        return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
    }

    private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(List<FeatureEvolution> featureEvolutions) {
        return new GeoJsonFeatureCollection(CollectionUtils.collect(featureEvolutions, featureEvolution2GeoJsonFeatureTransformer));
    }
}
