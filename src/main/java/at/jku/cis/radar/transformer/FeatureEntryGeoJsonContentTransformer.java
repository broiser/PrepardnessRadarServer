package at.jku.cis.radar.transformer;

import java.util.List;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.builder.GeoJsonContentBuilder;
import at.jku.cis.radar.geojson.GeoJsonContent;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;

public class FeatureEntryGeoJsonContentTransformer implements Transformer<FeatureEntry, GeoJsonContent> {

    @Override
    public GeoJsonContent transform(FeatureEntry featureEntry) {
        GeometryEvolutionEntry firstGeometryEvolutionEntry = getFirstGeometryEvolutionEntry(featureEntry);
        GeometryEvolutionEntry lastGeometryEvolutionEntry = getLastGeometryEvolutionEntry(featureEntry);

        GeoJsonContentBuilder builder = new GeoJsonContentBuilder();
        builder.withTitle(featureEntry.getTitle());
        builder.withEvent(featureEntry.getEvent());
        builder.withFeatureGroup(featureEntry.getFeatureGroup());
        builder.withDescription(featureEntry.getDescription());
        builder.withCreator(firstGeometryEvolutionEntry.getAccount().getUsername());
        builder.withModifier(lastGeometryEvolutionEntry.getAccount().getUsername());
        builder.withCreationDate(firstGeometryEvolutionEntry.getDate());
        builder.withModifiedDate(lastGeometryEvolutionEntry.getDate());
        return builder.build();
    }

    private GeometryEvolutionEntry getLastGeometryEvolutionEntry(FeatureEntry featureEntry) {
        int size = featureEntry.getGeometryEvolutionEntries().size();
        return size < 1 ? null : featureEntry.getGeometryEvolutionEntries().get(size - 1);
    }

    private GeometryEvolutionEntry getFirstGeometryEvolutionEntry(FeatureEntry featureEntry) {
        List<GeometryEvolutionEntry> geometryEvolutionEntries = featureEntry.getGeometryEvolutionEntries();
        return geometryEvolutionEntries.isEmpty() ? null : geometryEvolutionEntries.get(0);
    }
}
