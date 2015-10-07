package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureEvoluation;

@ApplicationScoped
public class FeatureEvoluation2FeatureTransformer implements Transformer<FeatureEvoluation, Feature> {

    @Override
    public Feature transform(FeatureEvoluation featureEvoluation) {
        return featureEvoluation.getFeature();
    }
}
