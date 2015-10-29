package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolution2GroupTransformer implements Transformer<FeatureEvolution, Long> {

    @Inject
    private FeatureEvolution2FeatureTransformer featureEvolution2FeatureTransformer;

    @Override
    public Long transform(FeatureEvolution featureEvolution) {
        return featureEvolution2FeatureTransformer.transform(featureEvolution).getFeatureGroup();
    }
}
