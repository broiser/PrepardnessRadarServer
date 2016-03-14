package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.model.v2.FeatureEntry;

@ApplicationScoped
public class FeatureEntryService implements Serializable {

    @Inject
    private FeatureEntryDao featureEntryDao;

    public List<FeatureEntry> getFeatureEntry(long eventId) {
        return featureEntryDao.findFeaturesByEvent(eventId);
    }

}