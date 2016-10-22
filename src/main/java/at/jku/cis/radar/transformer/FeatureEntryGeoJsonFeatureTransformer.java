package at.jku.cis.radar.transformer;

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
        GeoJsonFeatureBuilder geoJsonFeatureBuilder = new GeoJsonFeatureBuilder();
        geoJsonFeatureBuilder.withFeatureGroup(featureEntry.getFeatureGroup());
        geoJsonFeatureBuilder.withGeometry(combineGeometry(featureEntry));
        geoJsonFeatureBuilder.withLastModifiedDate(getLastGeometryEvolutionEntry(featureEntry).getDate());
        return geoJsonFeatureBuilder.build();
    }

    private Geometry combineGeometry(FeatureEntry featureEntry) {
        GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(new Geometry[0]);
        for (GeometryEvolutionEntry geometryEvolutionEntry : featureEntry.getGeometryEvolutionEntries()) {
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

    private GeometryEvolutionEntry getLastGeometryEvolutionEntry(FeatureEntry featureEntry) {
        int size = featureEntry.getGeometryEvolutionEntries().size();
        return size < 1 ? null : featureEntry.getGeometryEvolutionEntries().get(size - 1);
    }

}
