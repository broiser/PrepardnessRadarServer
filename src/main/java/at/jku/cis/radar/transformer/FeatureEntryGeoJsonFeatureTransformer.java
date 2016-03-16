package at.jku.cis.radar.transformer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.Transformer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.builder.GeoJsonFeatureBuilder;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;
import at.jku.cis.radar.service.GeometryService;

@ApplicationScoped
public class FeatureEntryGeoJsonFeatureTransformer implements Transformer<FeatureEntry, GeoJsonFeature> {

    @Inject
    private GeometryService geometryService;
    @Inject
    private GeometryFactory geometryFactory;

    @Override
    public GeoJsonFeature transform(FeatureEntry featureEntry) {
        List<GeometryEvolutionEntry> geometryEvolutionEntries = featureEntry.getGeometryEvolutionEntries();

        GeoJsonFeatureBuilder geoJsonFeatureBuilder = new GeoJsonFeatureBuilder();
        geoJsonFeatureBuilder.withFeatureGroup(featureEntry.getFeatureGroup());
        geoJsonFeatureBuilder.withProperties(combineProperties(geometryEvolutionEntries));
        geoJsonFeatureBuilder.withGeometry(combineGeometry(geometryEvolutionEntries));
        return geoJsonFeatureBuilder.build();
    }

    private Map<String, Object> combineProperties(List<GeometryEvolutionEntry> geometryEvolutionEntries) {
        Map<String, Object> properties = new HashMap<>();
        for (GeometryEvolutionEntry geometryEvolutionEntry : geometryEvolutionEntries) {
            properties.putAll(geometryEvolutionEntry.getProperties());
        }
        return properties;
    }

    private Geometry combineGeometry(List<GeometryEvolutionEntry> geometryEvolutionEntries) {
        GeometryCollection geometryCollection = createEmptyGeometryCollection();
        for (GeometryEvolutionEntry geometryEvolutionEntry : geometryEvolutionEntries) {
            for (GeometryEntry geometryEntry : geometryEvolutionEntry.getGeometryEntries()) {
                geometryCollection = combineGeometry(geometryCollection, geometryEntry);
            }
        }
        return geometryCollection;
    }

    private GeometryCollection combineGeometry(GeometryCollection geometryCollection, GeometryEntry geometryEntry) {
        switch (geometryEntry.getStatus()) {
        case CREATED:
            return unionGeometries(geometryCollection, geometryEntry);
        case ERASED:
            return differenceGeometries(geometryCollection, geometryEntry);
        default:
            throw new IllegalArgumentException("Status doesn't exist.");
        }
    }

    private GeometryCollection differenceGeometries(GeometryCollection geometryCollection,
            GeometryEntry geometryEntry) {
        return geometryService.difference(geometryCollection, geometryEntry.getGeometry());
    }

    private GeometryCollection unionGeometries(GeometryCollection geometryCollection, GeometryEntry geometryEntry) {
        return geometryService.union(geometryCollection, (GeometryCollection) geometryEntry.getGeometry());
    }

    @SuppressWarnings("static-access")
    private GeometryCollection createEmptyGeometryCollection() {
        Geometry[] geometries = geometryFactory.toGeometryArray(Collections.emptyList());
        return new GeometryCollection(geometries, geometryFactory);
    }
}
