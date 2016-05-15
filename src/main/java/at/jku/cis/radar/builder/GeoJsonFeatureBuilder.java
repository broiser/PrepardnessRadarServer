
package at.jku.cis.radar.builder;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonFeature;

public class GeoJsonFeatureBuilder implements Serializable {

    private GeoJsonFeature geoJsonFeature = new GeoJsonFeature();

    public GeoJsonFeatureBuilder withGeometry(Geometry geometry) {
        geoJsonFeature.setGeometry(geometry);
        return this;
    }

    public GeoJsonFeatureBuilder withFeatureGroup(long featureGroup) {
        geoJsonFeature.setFeatureGroup(featureGroup);
        return this;
    }

    public GeoJsonFeature build() {
        return geoJsonFeature;
    }
}
