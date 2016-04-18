package at.jku.cis.radar.rest;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.annotations.Secured;
import at.jku.cis.radar.builder.GeoJsonFeatureEvolutionBuilder;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.service.GeometryService;

@Secured
@Path("featureEvolution")
public class FeatureEvolutionRestService extends RestService {

    @Inject
    private FeatureEntryService featureEntryService;
    @Inject
    private GeometryService geometryService;
    @Inject
    private GeometryFactory geometryFactory;

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
        return Response.ok(createGeoJsonFeatureCollection(featureEntry, from, to)).build();
    }

    private GeoJsonFeatureCollection createGeoJsonFeatureCollection(FeatureEntry featureEntry, long from, long to) {
        SortedMap<Date, GeoJsonFeatureEvolution> sortedMap = combineEditGeometries(featureEntry);
        GeoJsonFeatureCollection geoJsonFeatureCollection = new GeoJsonFeatureCollection();
        geoJsonFeatureCollection.getFeatures().addAll(sortedMap.subMap(new Date(from), new Date(to)).values());
        return geoJsonFeatureCollection;
    }

    private SortedMap<Date, GeoJsonFeatureEvolution> combineEditGeometries(FeatureEntry featureEntry) {
        TreeMap<Date, GeoJsonFeatureEvolution> geoJsonFeatureMap = new TreeMap<>();
        for (GeometryEvolutionEntry geometryEvolutionEntry : featureEntry.getGeometryEvolutionEntries()) {
            GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(new Geometry[0]);
            GeometryCollection erasedGeometryCollection = geometryFactory.createGeometryCollection(new Geometry[0]);
            for (GeometryEntry geometryEntry : geometryEvolutionEntry.getGeometryEntries()) {
                GeoJsonFeatureEvolutionBuilder geoJsonFeatureEvolutionBuilder = new GeoJsonFeatureEvolutionBuilder()
                        .withFeatureGroup(featureEntry.getFeatureGroup()).withDate(geometryEvolutionEntry.getDate());

                switch (geometryEntry.getStatus()) {
                case CREATED:
                    geometryCollection = geometryService.union(geometryCollection,
                            (GeometryCollection) geometryEntry.getGeometry());
                    geoJsonFeatureEvolutionBuilder.withGeometry(geometryCollection).withStatus(GeoJsonStatus.CREATED);
                    break;
                case ERASED:
                    if (geometryCollection.isEmpty()) {
                        erasedGeometryCollection = (GeometryCollection) geometryEntry.getGeometry();
                    } else {
                        geometryCollection = geometryService.difference(geometryCollection,
                                geometryEntry.getGeometry());
                        if (!erasedGeometryCollection.isEmpty()) {
                            erasedGeometryCollection = geometryService.union(erasedGeometryCollection,
                                    (GeometryCollection) geometryEntry.getGeometry());
                        }
                        geoJsonFeatureEvolutionBuilder.withStatus(GeoJsonStatus.ERASED);
                    }
                    break;
                }
                geoJsonFeatureMap.put(geometryEvolutionEntry.getDate(),
                        geoJsonFeatureEvolutionBuilder.withGeometry(geometryCollection).build());
                if (!erasedGeometryCollection.isEmpty()) {
                    GeoJsonFeatureEvolution geoJsonFeatureEvolutionErased = new GeoJsonFeatureEvolutionBuilder()
                            .withDate(geometryEvolutionEntry.getDate()).withFeatureGroup(featureEntry.getFeatureGroup())
                            .withGeometry(erasedGeometryCollection).withStatus(GeoJsonStatus.ERASED).build();
                    geoJsonFeatureMap.put(geometryEvolutionEntry.getDate(), geoJsonFeatureEvolutionErased);
                }
            }

        }
        return geoJsonFeatureMap;
    }
}
