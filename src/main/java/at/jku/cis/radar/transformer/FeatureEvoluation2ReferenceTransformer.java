package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.FeatureEvoluation;

@ApplicationScoped
public class FeatureEvoluation2ReferenceTransformer implements Transformer<FeatureEvoluation, String> {

    @Override
    public String transform(FeatureEvoluation featureEvoluation) {
        return featureEvoluation.getFeature().getFeatureReference();
    }
}
