package at.jku.cis.radar.model.rest;

import java.util.ArrayList;
import java.util.List;

public class FeatureCollection extends GeoJsonObject {

    private List<Feature> features = new ArrayList<Feature>();

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
