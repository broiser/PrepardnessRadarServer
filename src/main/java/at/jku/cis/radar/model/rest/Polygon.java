package at.jku.cis.radar.model.rest;

import java.util.List;

public class Polygon extends Geometry<List<LngLat>> {

    @Override
    public String toString() {
        return "Polygon{} " + super.toString();
    }

}
