package at.jku.cis.radar.model.rest;

public class Point extends GeoJsonObject {

    private LngLat coordinates;

    public Point() {
    }

    public Point(LngLat coordinates) {
        this.coordinates = coordinates;
    }

    public LngLat getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LngLat coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Point{" + "coordinates=" + coordinates + "} " + super.toString();
    }
}
