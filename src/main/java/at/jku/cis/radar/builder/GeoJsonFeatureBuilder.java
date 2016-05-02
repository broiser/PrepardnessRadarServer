
package at.jku.cis.radar.builder;

import static at.jku.cis.radar.geojson.GeoJsonObject.CREATION_DATE;
import static at.jku.cis.radar.geojson.GeoJsonObject.CREATOR;
import static at.jku.cis.radar.geojson.GeoJsonObject.DESCRIPTION;
import static at.jku.cis.radar.geojson.GeoJsonObject.MODIFIED_DATE;
import static at.jku.cis.radar.geojson.GeoJsonObject.MODIFIER;
import static at.jku.cis.radar.geojson.GeoJsonObject.TITLE;

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

    public GeoJsonFeatureBuilder withTitle(String title) {
        return withProperty(TITLE, title);
    }

    public GeoJsonFeatureBuilder withDescription(String description) {
        return withProperty(DESCRIPTION, description);
    }

    public GeoJsonFeatureBuilder withCreator(String username) {
        return withProperty(CREATOR, username);
    }

    public GeoJsonFeatureBuilder withCreationDate(String creationDate) {
        return withProperty(CREATION_DATE, creationDate);
    }

    public GeoJsonFeatureBuilder withModifier(String username) {
        return withProperty(MODIFIER, username);
    }

    public GeoJsonFeatureBuilder withModifiedDate(String modifiedDate) {
        return withProperty(MODIFIED_DATE, modifiedDate);
    }
    
    private GeoJsonFeatureBuilder withProperty(String label, Object value) {
        geoJsonFeature.getProperties().put(label, value);
        return this;
    }

    public GeoJsonFeature build() {
        return geoJsonFeature;
    }
}
