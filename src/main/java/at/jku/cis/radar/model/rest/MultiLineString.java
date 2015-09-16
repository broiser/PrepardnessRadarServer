package at.jku.cis.radar.model.rest;

import java.util.List;

public class MultiLineString extends Geometry<List<LngLat>> {

    public MultiLineString() {
    }

    @Override
    public String toString() {
        return "MultiLineString{} " + super.toString();
    }
}
