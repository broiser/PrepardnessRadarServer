package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.dao.FeatureEvoluationDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureEvoluation;

@ApplicationScoped
public class FeatureEvoluationService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureEvoluationDao featureEvoluationDao;

    public FeatureEvoluation findLatest(long eventId) {
        return featureEvoluationDao.findLatest(eventId);
    }

    @Transactional
    public void save(long eventId, List<Feature> features) {
        Event event = eventDao.findById(eventId);
        if (event == null) {
            throw new IllegalArgumentException();
        }
        Date date = DateTime.now().toDate();
        for (Feature feature : features) {
            featureEvoluationDao.save(createFeatureEvoluation(event, feature, date));
        }
    }

    private FeatureEvoluation createFeatureEvoluation(Event event, Feature feature, Date date) {
        FeatureEvoluation featureEvoluation = new FeatureEvoluation();
        featureEvoluation.setDate(date);
        featureEvoluation.setEvent(event);
        featureEvoluation.setFeature(feature);
        return featureEvoluation;
    }
}
