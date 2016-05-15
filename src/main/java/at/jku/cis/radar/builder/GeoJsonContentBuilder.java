
package at.jku.cis.radar.builder;

import java.io.Serializable;
import java.util.Date;

import at.jku.cis.radar.geojson.GeoJsonContent;
import at.jku.cis.radar.model.Event;

public class GeoJsonContentBuilder implements Serializable {

    private GeoJsonContent geoJsonContent = new GeoJsonContent();

    public GeoJsonContentBuilder withTitle(String title) {
        geoJsonContent.setTitle(title);
        return this;
    }

    public GeoJsonContentBuilder withFeatureGroup(long featureGroup) {
        geoJsonContent.setFeatureGroup(featureGroup);
        return this;
    }

    public GeoJsonContentBuilder withEvent(Event event) {
        geoJsonContent.setEvent(event);
        return this;
    }

    public GeoJsonContentBuilder withDescription(String description) {
        geoJsonContent.setDescription(description);
        return this;
    }

    public GeoJsonContentBuilder withCreator(String creator) {
        geoJsonContent.setCreator(creator);
        return this;
    }

    public GeoJsonContentBuilder withModifier(String modifier) {
        geoJsonContent.setModifier(modifier);
        return this;
    }

    public GeoJsonContentBuilder withCreationDate(Date creationDate) {
        geoJsonContent.setCreationDate(creationDate);
        return this;
    }

    public GeoJsonContentBuilder withModifiedDate(Date modifiedDate) {
        geoJsonContent.setModifiedDate(modifiedDate);
        return this;
    }

    public GeoJsonContent build() {
        return geoJsonContent;
    }

}
