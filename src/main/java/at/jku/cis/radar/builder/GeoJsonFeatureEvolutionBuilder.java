package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;

public class GeoJsonFeatureEvolutionBuilder implements Serializable {

    private GeoJsonFeatureEvolution geoJsonFeatureEvolution = new GeoJsonFeatureEvolution();

    public GeoJsonFeatureEvolutionBuilder withGeometry(Geometry geometry) {
        geoJsonFeatureEvolution.setGeometry(geometry);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withFeatureGroup(long featureGroup) {
        geoJsonFeatureEvolution.setFeatureGroup(featureGroup);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withStatus(GeoJsonStatus status) {
        geoJsonFeatureEvolution.setStatus(status);
        return this;
    }

    public GeoJsonFeatureEvolutionBuilder withCreationDate(Date date) {
        geoJsonFeatureEvolution.setCreationDate(date);
        return this;
    }
    
    public GeoJsonFeatureEvolution build() {
        return geoJsonFeatureEvolution;
    }

}
