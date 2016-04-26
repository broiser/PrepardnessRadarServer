
package at.jku.cis.radar.geojson;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GeoJsonFeatureEvolution extends GeoJsonFeature {

    @JsonIgnore
    public Date getDate() {
        return (Date) getProperties().get(CREATION_DATE);
    }

    public void setCreationDate(Date date) {
        getProperties().put(CREATION_DATE, date);
    }

    public void setStatus(GeoJsonStatus status) {
        getProperties().put(STATUS, status.name());
    }
}
