package at.jku.cis.radar.model.rest;

import java.util.List;

public class MultiPolygon extends Geometry<List<List<LngLat>>> {

    @Override
    public String toString() {
        return "MultiPolygon{} " + super.toString();
    }
}
