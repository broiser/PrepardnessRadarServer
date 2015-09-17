package at.jku.cis.radar.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

@JsonSubTypes(@Type(Feature.class))
@JsonTypeInfo(property = GeoJsonObject.TYPE, use = Id.NAME)
public abstract class GeoJsonObject extends BaseEntity {

    static final String TYPE = "type";

}
