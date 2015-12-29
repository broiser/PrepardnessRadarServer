package at.jku.cis.radar.geojson;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonFeatureCollection implements GeoJsonObject {

    private List<GeoJsonFeature> geoJsonFeatures = new ArrayList<GeoJsonFeature>();

    public GeoJsonFeatureCollection() {
    }

    public GeoJsonFeatureCollection(GeoJsonFeature geoJsonFeature) {
        this.geoJsonFeatures.add(geoJsonFeature);
    }

    public GeoJsonFeatureCollection(List<? extends GeoJsonFeature> geoJsonFeatures) {
        this.geoJsonFeatures.addAll(geoJsonFeatures);
    }

    public List<GeoJsonFeature> getFeatures() {
        return geoJsonFeatures;
    }

    public void setFeatures(List<GeoJsonFeature> geoJsonFeatures) {
        this.geoJsonFeatures = geoJsonFeatures;
    }
}
