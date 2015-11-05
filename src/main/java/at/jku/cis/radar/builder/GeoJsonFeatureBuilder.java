package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.Feature;

public class GeoJsonFeatureBuilder implements Serializable {

    private Feature geoJsonFeature = new Feature();

    public GeoJsonFeatureBuilder withGeometry(Geometry geometry) {
        geoJsonFeature.setGeometry(geometry);
        return this;
    }

    public GeoJsonFeatureBuilder withFeatureGroup(long featureGroup) {
        geoJsonFeature.setFeatureGroup(featureGroup);
        return this;
    }

    public GeoJsonFeatureBuilder withProperties(Map<String, Object> properties) {
        geoJsonFeature.setProperties(properties);
        return this;
    }

    public Feature build() {
        return geoJsonFeature;
    }

}
