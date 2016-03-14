package at.jku.cis.radar.service.v2;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.jku.cis.radar.dao.FeatureGroupDao;

@ApplicationScoped
public class FeatureGroupService implements Serializable {
    private static final long ONE = 1;
    private static final long ZERO = 0;

    @Inject
    private FeatureGroupDao featureGroupDao;

    private Long featureGroup;

    public synchronized long generateNextFeatureGroup() {
        if (featureGroup == null) {
            featureGroup = loadNextFeatureGroup();
        }
        return featureGroup++;
    }

    private long loadNextFeatureGroup() {
        Long featureGroup = featureGroupDao.findNextFeatureGroup();
        if (featureGroup == null) {
            featureGroup = ZERO;
        }
        return featureGroup + ONE;
    }
}
