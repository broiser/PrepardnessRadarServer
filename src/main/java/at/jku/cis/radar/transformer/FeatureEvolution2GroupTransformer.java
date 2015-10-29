package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolution2GroupTransformer implements Transformer<FeatureEvolution, Long> {

    @Override
    public Long transform(FeatureEvolution featureEvolution) {
        return featureEvolution.getFeatureGroup();
    }
}
