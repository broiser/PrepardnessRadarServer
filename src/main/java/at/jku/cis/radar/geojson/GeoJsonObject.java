package at.jku.cis.radar.geojson;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

//@formatter:off
@JsonTypeInfo(property = GeoJsonObject.TYPE, use = Id.NAME)
@JsonSubTypes({ 
    @Type(name = GeoJsonObject.FEATURE, value = GeoJsonFeature.class),
    @Type(name = GeoJsonObject.FEATURE, value = GeoJsonFeatureEvolution.class),
    @Type(name = GeoJsonObject.FEATURE_COLLECTION, value = GeoJsonFeatureCollection.class)})
//@formatter:on
public interface GeoJsonObject extends Serializable {

    static final String TYPE = "type";
    static final String FEATURE = "Feature";
    static final String FEATURE_COLLECTION = "FeatureCollection";

    static final String FEATURE_ID = "id";
    static final String COORDINATES = "coordinates";
    static final String GEOMETRIES = "geometries";

    static final String TITLE = "title";
    static final String STATUS = "status";
    static final String DESCRIPTION = "description";
    static final String CREATOR = "creator";
    static final String MODIFIER = "editor";
    static final String CREATION_DATE = "creation_date";
    static final String MODIFIED_DATE = "modified_date";

}
