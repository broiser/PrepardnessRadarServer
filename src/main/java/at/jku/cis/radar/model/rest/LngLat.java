package at.jku.cis.radar.model.rest;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import at.jku.cis.radar.rest.LngLatDeserializer;
import at.jku.cis.radar.rest.LngLatSerializer;

@JsonSerialize(using = LngLatSerializer.class)
@JsonDeserialize(using = LngLatDeserializer.class)
public class LngLat implements Serializable {

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LngLat)) {
            return false;
        }
        return equals((LngLat) obj);
    }

    public boolean equals(LngLat lngLat) {
        return lngLat.latitude == latitude && lngLat.longitude == longitude;
    }

    @Override
    public String toString() {
        return "LngLatAlt{" + "longitude=" + longitude + ", latitude=" + latitude + '}';
    }
}
