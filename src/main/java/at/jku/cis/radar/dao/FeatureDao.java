package at.jku.cis.radar.dao;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.Feature;

@ApplicationScoped
public class FeatureDao extends AbstractDao<Feature> {

    public FeatureDao() {
        super(Feature.class);
    }
}