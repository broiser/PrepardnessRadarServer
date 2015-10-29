package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolution2FeatureTransformer implements Transformer<FeatureEvolution, Feature> {

    @Override
    public Feature transform(FeatureEvolution featureEvolution) {
        Feature feature = new Feature();
        feature.setGeometry(featureEvolution.getGeometry());
        feature.setFeatureGroup(featureEvolution.getFeatureGroup());
        feature.setProperties(featureEvolution.getProperties());
        return feature; 
    }
}
