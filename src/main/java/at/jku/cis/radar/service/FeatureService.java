package at.jku.cis.radar.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import at.jku.cis.radar.dao.FeatureDao;
import at.jku.cis.radar.model.Feature;

@ApplicationScoped
public class FeatureService implements Serializable {

    @Inject
    private FeatureDao featureDao;
    @Inject
    private FeatureGroupService featureReferenceService;

    @Transactional
    public Feature save(Feature feature) {
        if (feature.getId() >= 1) {
            return featureDao.save(feature);
        }
        return featureDao.create(createFeature(feature));
    }

    private Feature createFeature(Feature feature) {
        Feature createdFeature = new Feature();
        createdFeature.setGeometry(feature.getGeometry());
        createdFeature.setProperties(feature.getProperties());
        if (feature.getFeatureGroup() <= 0) {
            createdFeature.setFeatureGroup(featureReferenceService.generateNextFeatureGroup());
        }
        return createdFeature;
    }
}
