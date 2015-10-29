package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolution2FeatureTransformer implements Transformer<FeatureEvolution, Feature> {

    @Override
    public Feature transform(FeatureEvolution featureEvolution) {
        return featureEvolution.getFeature();
    }
}
