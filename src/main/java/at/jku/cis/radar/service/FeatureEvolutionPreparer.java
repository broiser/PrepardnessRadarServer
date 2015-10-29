package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import at.jku.cis.radar.builder.FeatureBuilder;
import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionPreparer implements Serializable {

    public List<Feature> prepareEvolution(List<FeatureEvolution> featureEvolutions) {
        if (featureEvolutions.isEmpty()) {
            return Collections.emptyList();
        }
        return createFeatureEvolution(featureEvolutions);
    }

    private List<Feature> createFeatureEvolution(List<FeatureEvolution> featureEvolutions) {
        List<Feature> features = new ArrayList<>();

        for (int i = featureEvolutions.size() - 1; i >= 0; i--) {
            FeatureEvolution featureEvolution = featureEvolutions.get(i);
            features = createFeatureEvolution(features, featureEvolution);
        }

        return features;
    }

    private List<Feature> createFeatureEvolution(List<Feature> currentFeatures, FeatureEvolution featureEvolution) {
        List<Feature> features = new ArrayList<>();
        long featureGroup = featureEvolution.getFeatureGroup();
        Map<String, Object> properties = featureEvolution.getProperties();
        GeometryCollection geometryCollection = (GeometryCollection) featureEvolution.getGeometry();

        for (int n = 0; n < geometryCollection.getNumGeometries(); n++) {
            Geometry geometry = geometryCollection.getGeometryN(n);

            if (currentFeatures.isEmpty()) {
                features.add(buildFeature(featureGroup, geometry, properties));
            } else {
                for (Feature currentFeature : currentFeatures) {
                    features.addAll(intersectFeature(currentFeature, geometry));
                }
            }
        }
        return features;
    }

    private List<Feature> intersectFeature(Feature feature, Geometry geometry) {
        List<Feature> features = new ArrayList<>();

        Map<String, Object> properties = feature.getProperties();
        long featureGroup = feature.getFeatureGroup();

        if (feature.getGeometry().disjoint(geometry)) {
            features.add(feature);
        } else {
            Geometry newGeometry = feature.getGeometry().difference(geometry);
            features.add(buildFeature(featureGroup, newGeometry, properties));
            features.add(buildFeature(featureGroup, geometry, properties));
        }
        return features;
    }

    private Feature buildFeature(long featureGroup, Geometry geometry, Map<String, Object> properties) {
        FeatureBuilder featureBuilder = new FeatureBuilder().withFeatureGroup(featureGroup);
        return featureBuilder.withGeometry(geometry).withProperties(properties).build();
    }
}
