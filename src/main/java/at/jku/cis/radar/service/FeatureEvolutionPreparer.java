package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.transformer.FeatureEvolution2FeatureTransformer;

@ApplicationScoped
public class FeatureEvolutionPreparer implements Serializable {

    @Inject
    private FeatureEvolution2FeatureTransformer featureEvolution2FeatureTransformer;

    public Map<Date, List<Feature>> prepareEvolution(List<FeatureEvolution> featureEvolutions) {
        if (featureEvolutions.isEmpty()) {
            return new HashMap<>();
        }

        Map<Date, List<Feature>> date2Feature = new HashMap<>();
        FeatureEvolution featureEvolution = featureEvolutions.get(featureEvolutions.size() - 1);
        List<Feature> lastFeatures = Arrays.asList(transformToFeature(featureEvolution));

        date2Feature.put(featureEvolution.getDate(), lastFeatures);
        for (int i = featureEvolutions.size() - 2; i >= 0; i--) {
            FeatureEvolution currentEvolution = featureEvolutions.get(i);
            Feature currentFeature = transformToFeature(currentEvolution);
            date2Feature.put(currentEvolution.getDate(), prepareEvolution(lastFeatures, currentFeature));
        }
        return date2Feature;
    }

    private List<Feature> prepareEvolution(List<Feature> lastFeatures, Feature currentFeature) {
        List<Feature> features = new ArrayList<>();
        for (Feature lastFeature : lastFeatures) {
            features.addAll(prepareEvolution(currentFeature, lastFeature));
        }
        return features;
    }

    @SuppressWarnings("unused")
    private List<Feature> prepareEvolution(Feature currentFeature, Feature lastFeature) {
        GeometryCollection lastGeometryCollection = extractGeometryCollection(lastFeature);
        GeometryCollection currentGeometryCollection = extractGeometryCollection(currentFeature);
        for (int i = 0; i < lastGeometryCollection.getNumGeometries(); i++) {
            Geometry lastGeometry = lastGeometryCollection.getGeometryN(i);
            for (int j = 0; j < currentGeometryCollection.getNumGeometries(); j++) {
                Geometry currentGeometry = currentGeometryCollection.getGeometryN(j);
                if (lastGeometry.intersects(currentGeometry)) {
                    Geometry geometry = currentGeometry.difference(lastGeometry);
                }
            }
        }
        return new ArrayList<>();
    }

    private Feature transformToFeature(FeatureEvolution featureEvolution) {
        return featureEvolution2FeatureTransformer.transform(featureEvolution);
    }

    private GeometryCollection extractGeometryCollection(Feature feature) {
        return (GeometryCollection) feature.getGeometry();
    }
}
