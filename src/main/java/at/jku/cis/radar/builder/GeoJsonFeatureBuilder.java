
package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonStatus;

public class GeoJsonFeatureBuilder implements Serializable {

	private GeoJsonFeature geoJsonFeature = new GeoJsonFeature();

	public GeoJsonFeatureBuilder withStatus(GeoJsonStatus status) {
		geoJsonFeature.getProperties().put("status", status);
		return this;
	}

	public GeoJsonFeatureBuilder withDate(Date date) {
		geoJsonFeature.getProperties().put("date", date);
		return this;
	}

	public GeoJsonFeatureBuilder withGeometry(Geometry geometry) {
		geoJsonFeature.setGeometry(geometry);
		return this;
	}

	public GeoJsonFeatureBuilder withFeatureGroup(long featureGroup) {
		geoJsonFeature.setFeatureGroup(featureGroup);
		return this;
	}
	
	public GeoJsonFeatureBuilder withProperties(Map<String, Object> properties) {
		geoJsonFeature.getProperties().putAll(properties);
		return this;
	}

	public GeoJsonFeature build() {
		return geoJsonFeature;
	}
}
