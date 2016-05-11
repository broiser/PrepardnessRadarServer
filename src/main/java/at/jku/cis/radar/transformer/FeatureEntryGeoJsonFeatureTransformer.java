package at.jku.cis.radar.transformer;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.Transformer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.builder.GeoJsonFeatureBuilder;
import at.jku.cis.radar.comparator.DateMarshaller;
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
    @Inject
    private DateMarshaller dateMarshaller;

    @Override
    public GeoJsonFeature transform(FeatureEntry featureEntry) {
        GeometryEvolutionEntry firstGeometryEvolutionEntry = getFirstGeometryEvolutionEntry(featureEntry);
        GeometryEvolutionEntry lastGeometryEvolutionEntry = getLastGeometryEvolutionEntry(featureEntry);
        
        GeoJsonFeatureBuilder geoJsonFeatureBuilder = new GeoJsonFeatureBuilder();
        geoJsonFeatureBuilder.withTitle(featureEntry.getTitle());
        geoJsonFeatureBuilder.withFeatureGroup(featureEntry.getFeatureGroup());
        geoJsonFeatureBuilder.withDescription(featureEntry.getDescription());
        geoJsonFeatureBuilder.withCreator(firstGeometryEvolutionEntry.getAccount().getUsername());
        geoJsonFeatureBuilder.withModifier(lastGeometryEvolutionEntry.getAccount().getUsername());
        geoJsonFeatureBuilder.withCreationDate(dateMarshaller.marshal(firstGeometryEvolutionEntry.getDate()));
        geoJsonFeatureBuilder.withModifiedDate(dateMarshaller.marshal(lastGeometryEvolutionEntry.getDate()));
        geoJsonFeatureBuilder.withGeometry(combineGeometry(featureEntry.getGeometryEvolutionEntries()));
        return geoJsonFeatureBuilder.build();
    }

    private GeometryEvolutionEntry getLastGeometryEvolutionEntry(FeatureEntry featureEntry) {
        int size = featureEntry.getGeometryEvolutionEntries().size();
        return size < 1 ? null : featureEntry.getGeometryEvolutionEntries().get(size - 1);
    }

    private GeometryEvolutionEntry getFirstGeometryEvolutionEntry(FeatureEntry featureEntry) {
        List<GeometryEvolutionEntry> geometryEvolutionEntries = featureEntry.getGeometryEvolutionEntries();
        return geometryEvolutionEntries.isEmpty() ? null : geometryEvolutionEntries.get(0);
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
