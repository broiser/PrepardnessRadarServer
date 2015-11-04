package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;

public class GeoJsonFeatureEvolutionBuilder implements Serializable {

    private GeoJsonFeatureEvolution geoJsonFeatureEvolution = new GeoJsonFeatureEvolution();

    public GeoJsonFeatureEvolutionBuilder withStatus(GeoJsonStatus status) {
        geoJsonFeatureEvolution.setStatus(status);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withDate(Date date) {
        geoJsonFeatureEvolution.setDate(date);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withGeometry(Geometry geometry) {
        geoJsonFeatureEvolution.setGeometry(geometry);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withFeatureGroup(long featureGroup) {
        geoJsonFeatureEvolution.setFeatureGroup(featureGroup);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withProperties(Map<String, Object> properties) {
        geoJsonFeatureEvolution.setProperties(properties);
        return this;
    }

    public GeoJsonFeatureEvolution build() {
        return geoJsonFeatureEvolution;
    }

}
