package at.jku.cis.radar.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.jku.cis.radar.dao.FeatureDao;
import at.jku.cis.radar.model.Feature;

@ApplicationScoped
public class FeatureGroupService implements Serializable {
    private static final int FIRST_FEATURE_GROUP = 1;

    @Inject
    private FeatureDao featureDao;

    private long featureGroup;

    @PostConstruct
    public void getLastFeatureGroup() {
        Feature lastFeature = featureDao.findLastFeature();
        // TODO Query anpassen
        if (lastFeature == null) {
            featureGroup = FIRST_FEATURE_GROUP;
        } else {
            featureGroup = lastFeature.getFeatureGroup();
        }
    }

    public synchronized long generateNextReference() {
        return featureGroup++;
    }
}
