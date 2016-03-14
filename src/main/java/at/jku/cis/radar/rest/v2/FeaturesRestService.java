package at.jku.cis.radar.rest.v2;

import java.util.ArrayList;
import java.util.Collection;
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
import at.jku.cis.radar.model.v2.FeatureEntry;
import at.jku.cis.radar.service.v2.FeatureEntryService;
import at.jku.cis.radar.transformer.v2.FeatureEntryGeoJsonFeatureTransformer;

@Path("features")
public class FeaturesRestService extends RestService {

    @Inject
    private FeatureEntryService featureEntryService;
    @Inject
    private FeatureEntryGeoJsonFeatureTransformer featureEntryGeoJsonFeatureTransformer;

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

        List<FeatureEntry> featureEntries = featureEntryService.findAllByEvent(eventId, fromDate, toDate);
        return Response.ok(buildGeoJsonFeatureCollection(featureEntries)).build();
    }


    @PUT
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response editFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        FeatureEntry featureEntry = editFeatureEntry(geoJsonFeature);
        return Response.ok(featureEntry.getFeatureGroup()).build();
    }

    @POST
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        FeatureEntry featureEntry = geoJsonFeature.getFeatureGroup() < 1 ? createFeatureEntry(eventId, geoJsonFeature)
                : evolveFeatureEntry(geoJsonFeature);
        return Response.ok(featureEntry.getFeatureGroup()).build();
    }

    private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(List<FeatureEntry> featureEntries) {
        Collection<GeoJsonFeature> geoJsonFeatures = CollectionUtils.collect(featureEntries,
                featureEntryGeoJsonFeatureTransformer);
        return new GeoJsonFeatureCollection(new ArrayList<GeoJsonFeature>(geoJsonFeatures));
    }

    private FeatureEntry createFeatureEntry(long eventId, GeoJsonFeature geoJsonFeature) {
        return featureEntryService.create(eventId, geoJsonFeature.getGeometry(), geoJsonFeature.getProperties());
    }

    private FeatureEntry editFeatureEntry(GeoJsonFeature geoJsonFeature) {
        long featureGroup = geoJsonFeature.getFeatureGroup();
        return featureEntryService.edit(featureGroup, geoJsonFeature.getGeometry(), geoJsonFeature.getProperties());
    }

    private FeatureEntry evolveFeatureEntry(GeoJsonFeature geoJsonFeature) {
        long featureGroup = geoJsonFeature.getFeatureGroup();
        return featureEntryService.evolve(featureGroup, geoJsonFeature.getGeometry(), geoJsonFeature.getProperties());
    }
}