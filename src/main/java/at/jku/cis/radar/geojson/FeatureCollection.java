package at.jku.cis.radar.geojson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureCollection implements GeoJsonObject {

    private List<Feature> features = new ArrayList<Feature>();

    public FeatureCollection() {
    }

    public FeatureCollection(Feature feature) {
        this.features.add(feature);
    }

    public FeatureCollection(Collection<Feature> features) {
        this.features.addAll(features);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
