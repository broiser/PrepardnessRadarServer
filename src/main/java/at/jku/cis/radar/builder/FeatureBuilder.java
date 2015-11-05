package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonFeature;

public class FeatureBuilder implements Serializable {

    private GeoJsonFeature feature = new GeoJsonFeature();

    public FeatureBuilder withGeometry(Geometry geometry) {
        feature.setGeometry(geometry);
        return this;
    }

    public FeatureBuilder withFeatureGroup(long featureGroup) {
        feature.setFeatureGroup(featureGroup);
        return this;
    }

    public FeatureBuilder withProperties(Map<String, Object> properties) {
        feature.setProperties(properties);
        return this;
    }

    public GeoJsonFeature build() {
        return feature;
    }

}
