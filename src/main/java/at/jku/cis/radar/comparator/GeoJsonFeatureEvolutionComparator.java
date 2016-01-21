package at.jku.cis.radar.comparator;

import java.util.Comparator;

import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;

public class GeoJsonFeatureEvolutionComparator implements Comparator<GeoJsonFeatureEvolution> {

    @Override
    public int compare(GeoJsonFeatureEvolution o1, GeoJsonFeatureEvolution o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

}
