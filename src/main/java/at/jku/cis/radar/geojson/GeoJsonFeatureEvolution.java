
package at.jku.cis.radar.geojson;

import java.util.Date;

public class GeoJsonFeatureEvolution extends GeoJsonFeature {
    private Date date;
    private GeoJsonStatus status;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoJsonStatus getStatus() {
        return status;
    }

    public void setStatus(GeoJsonStatus status) {
        this.status = status;
    }

}
