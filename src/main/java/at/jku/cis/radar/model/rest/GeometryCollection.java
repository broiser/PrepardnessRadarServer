package at.jku.cis.radar.model.rest;

import java.util.ArrayList;
import java.util.List;

public class GeometryCollection extends GeoJsonObject {

    private List<GeoJsonObject> geometries = new ArrayList<GeoJsonObject>();

    public List<GeoJsonObject> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<GeoJsonObject> geometries) {
        this.geometries = geometries;
    }

    public String toString() {
        return "GeometryCollection{" + "geometries=" + geometries + "} " + super.toString();
    }
}
