
package at.jku.cis.radar.geojson;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GeoJsonFeatureEvolution extends GeoJsonFeature {
    private static final String STATUS = "status";
    private static final String DATE = "date";

    @JsonIgnore
    public Date getDate() {
        return (Date) getProperties().get(DATE);
    }

    public void setDate(Date date) {
        getProperties().put(DATE, date);
    }

    @JsonIgnore
    public GeoJsonStatus getStatus() {
        return GeoJsonStatus.valueOf((String) getProperties().get(STATUS));
    }

    public void setStatus(GeoJsonStatus status) {
        getProperties().put(STATUS, status.name());
    }
}
