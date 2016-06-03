package at.jku.cis.radar.geojson;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geometry.GeometryDeserializer;
import at.jku.cis.radar.geometry.GeometrySerializer;

public class GeoJsonFeature implements GeoJsonObject {

    @JsonProperty(value = FEATURE_ID)
    private long featureGroup;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry geometry;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public GeoJsonFeature() {
    }

    public long getFeatureGroup() {
        return featureGroup;
    }

    public void setFeatureGroup(long featureGroup) {
        this.featureGroup = featureGroup;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        return geometry.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GeoJsonFeature)) {
            return false;
        }
        return ((GeoJsonFeature) obj).geometry.equals(geometry);
    }
}
