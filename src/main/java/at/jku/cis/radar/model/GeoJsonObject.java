package at.jku.cis.radar.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

@JsonTypeInfo(property = GeoJsonObject.TYPE, use = Id.NAME)
@JsonSubTypes(@Type(Feature.class))
public abstract class GeoJsonObject extends BaseEntity {

    static final String TYPE = "type";

}
