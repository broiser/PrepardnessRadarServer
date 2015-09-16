package at.jku.cis.radar.model.rest;

import java.util.ArrayList;
import java.util.List;

public abstract class Geometry<T> extends GeoJsonObject {
    protected List<T> coordinates = new ArrayList<T>();

    public Geometry() {
    }

    public List<T> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<T> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Geometry{" + "coordinates=" + coordinates + "} " + super.toString();
    }
}
