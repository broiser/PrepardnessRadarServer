package at.jku.cis.radar.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.geojson.GeoJsonObject;
import at.jku.cis.radar.geojson.GeometryDeserializer;
import at.jku.cis.radar.geojson.GeometrySerializer;

@Entity
@NamedQuery(name = Feature.FIND_BY_REFERENCE, query = "SELECT f FROM Feature f where f.featureReference = :featureReference")
public class Feature extends BaseEntity implements GeoJsonObject {

    public static final String FIND_BY_REFERENCE = "Feature.findByReference";

    @JsonProperty(value = FEATURE_ID)
    private String featureReference;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "geometry", columnDefinition = "Geometry")
    private Geometry geometry;
    @Transient
    private Map<String, Object> properties = new HashMap<String, Object>();

    public Feature() {
    }

    public Feature(String s) {

    }

    public String getFeatureReference() {
        return featureReference;
    }

    public void setFeatureReference(String featureReference) {
        this.featureReference = featureReference;
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
    public String toString() {
        return "Feature{properties=" + properties + ", geometry=" + geometry + ", id='" + getId() + "'}";
    }
}
