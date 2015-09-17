package at.jku.cis.radar.model;

import java.util.ArrayList;
import java.util.List;

import at.jku.cis.radar.geojson.GeoJsonObject;

public class FeatureCollection implements GeoJsonObject {

    private List<Feature> features = new ArrayList<Feature>();

    public FeatureCollection() {
    }

    public FeatureCollection(Feature feature) {
        features.add(feature);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
