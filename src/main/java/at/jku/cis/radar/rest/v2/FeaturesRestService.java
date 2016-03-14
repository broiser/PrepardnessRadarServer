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
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.model.v2.FeatureEntry;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.transformer.v2.FeatureEntryGeoJsonFeatureTransformer;

@Path("features")
public class FeaturesRestService extends RestService {

    private static final String MESSAGE = StringUtils.EMPTY;
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

    private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(List<FeatureEntry> featureEntries) {
        Collection<GeoJsonFeature> geoJsonFeatures = CollectionUtils.collect(featureEntries,
                featureEntryGeoJsonFeatureTransformer);
        return new GeoJsonFeatureCollection(new ArrayList<GeoJsonFeature>(geoJsonFeatures));
    }

    @PUT
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        throw new NotImplementedException(MESSAGE);
    }

    @POST
    @Path("{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
        throw new NotImplementedException(MESSAGE);
    }
}