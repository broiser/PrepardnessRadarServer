package at.jku.cis.radar.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonObject;
import at.jku.cis.radar.geojson.GeometryDeserializer;
import at.jku.cis.radar.geojson.GeometrySerializer;

@Entity
public class Feature extends BaseEntity implements GeoJsonObject {

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry geometry;
    @Transient
    private Map<String, Object> properties = new HashMap<String, Object>();

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
    public String toString() {
        return "Feature{properties=" + properties + ", geometry=" + geometry + ", id='" + getId() + "'}";
    }
}
