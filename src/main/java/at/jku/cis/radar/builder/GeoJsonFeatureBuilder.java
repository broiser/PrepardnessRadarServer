
package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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
    
    public GeoJsonFeatureBuilder withLastModifiedDate(Date date){
    	HashMap<String, Object> properties = new HashMap<>();
    	properties.put("date", date);
    	geoJsonFeature.setProperties(properties);
    	return this;
    }

    public GeoJsonFeature build() {
        return geoJsonFeature;
    }
}
