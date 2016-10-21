package at.jku.cis.radar.rest;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.joda.time.DateTime;

import at.jku.cis.radar.annotations.Secured;
import at.jku.cis.radar.comparator.GeoJsonFeatureEvolutionComparator;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.transformer.FeatureEntryGeoJsonFeatureEvolutionTransformer;

@Secured
@Path("featureEvolution")
public class FeatureEvolutionRestService extends RestService {

    @Inject
    private FeatureEntryService featureEntryService;
    @Inject
    private GeoJsonFeatureEvolutionComparator geoJsonFeatureEvolutionComparator;
    @Inject
    private FeatureEntryGeoJsonFeatureEvolutionTransformer featureEntryGeoJsonFeatureEvolutionTransformer;

    @GET
    @Path("{eventId}/{featureGroup}")
    public Response getFeatureEvolution(@PathParam("eventId") long eventId,
            @PathParam("featureGroup") long featureGroup) {
        DateTime dateTime = DateTime.now();
        long to = dateTime.getMillis();
        long from = dateTime.minusDays(1).getMillis();
        return getFeatureEvolution(eventId, featureGroup, from, to);
    }

    @GET
    @Path("{eventId}/{featureGroup}/{from}/{to}")
    public Response getFeatureEvolution(@PathParam("eventId") long eventId,
            @PathParam("featureGroup") long featureGroup, @PathParam("from") long from, @PathParam("to") long to) {
        FeatureEntry featureEntry = featureEntryService.findByFeatureGroup(featureGroup);
        return Response.ok(buildGeoJsonFeatureCollection(featureEntry, from, to)).build();
    }

    private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(FeatureEntry featureEntry, final long from,
            final long to) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEntryGeoJsonFeatureEvolutionTransformer
                .transform(featureEntry);
        Collections.sort(geoJsonFeatureEvolutions, geoJsonFeatureEvolutionComparator);
        CollectionUtils.filter(geoJsonFeatureEvolutions, new Predicate<GeoJsonFeatureEvolution>() {
            @Override
            public boolean evaluate(GeoJsonFeatureEvolution evolution) {
                long time = evolution.getDate().getTime();
                return from <= time && time <= to;
            }
        });
        return new GeoJsonFeatureCollection(geoJsonFeatureEvolutions);
    }
}
