
package at.jku.cis.radar.builder;

import static at.jku.cis.radar.geojson.GeoJsonObject.CREATION_DATE;
import static at.jku.cis.radar.geojson.GeoJsonObject.CREATOR;
import static at.jku.cis.radar.geojson.GeoJsonObject.MODIFIED_DATE;
import static at.jku.cis.radar.geojson.GeoJsonObject.MODIFIER;

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

    public void withCreator(String username) {
        geoJsonFeature.getProperties().put(CREATOR, username);
    }
    
    public void withCreationDate(String creationDate){
        geoJsonFeature.getProperties().put(CREATION_DATE, creationDate);
    }
    
    public void withModifier(String username){
        geoJsonFeature.getProperties().put(MODIFIER, username);
    }
    
    public void withModifiedDate(String modifiedDate){
        geoJsonFeature.getProperties().put(MODIFIED_DATE, modifiedDate);
    }

    public GeoJsonFeature build() {
        return geoJsonFeature;
    }
}
