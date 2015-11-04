package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.builder.GeoJsonFeatureBuilder;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolution2GeoJsonFeatureTransformer implements Transformer<FeatureEvolution, GeoJsonFeature> {

    @Override
    public GeoJsonFeature transform(FeatureEvolution featureEvolution) {
        GeoJsonFeatureBuilder geoJsonFeatureBuilder = new GeoJsonFeatureBuilder();
        geoJsonFeatureBuilder = geoJsonFeatureBuilder.withGeometry(featureEvolution.getGeometry());
        geoJsonFeatureBuilder = geoJsonFeatureBuilder.withFeatureGroup(featureEvolution.getFeatureGroup());
        geoJsonFeatureBuilder = geoJsonFeatureBuilder.withProperties(featureEvolution.getProperties());
        return geoJsonFeatureBuilder.build();
    }
}
