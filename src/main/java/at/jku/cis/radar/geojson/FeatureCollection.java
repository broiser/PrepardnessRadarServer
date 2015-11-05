package at.jku.cis.radar.geojson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureCollection implements GeoJsonObject {

    private List<Feature> geoJsonFeatures = new ArrayList<Feature>();

    public FeatureCollection() {
    }

    public FeatureCollection(Feature geoJsonFeature) {
        this.geoJsonFeatures.add(geoJsonFeature);
    }

    public FeatureCollection(Collection<Feature> geoJsonFeatures) {
        this.geoJsonFeatures.addAll(geoJsonFeatures);
    }

    public List<Feature> getFeatures() {
        return geoJsonFeatures;
    }

    public void setFeatures(List<Feature> geoJsonFeatures) {
        this.geoJsonFeatures = geoJsonFeatures;
    }
}
