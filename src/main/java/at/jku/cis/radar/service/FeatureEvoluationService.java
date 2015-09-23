package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.dao.FeatureDao;
import at.jku.cis.radar.dao.FeatureEvoluationDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureEvoluation;

@ApplicationScoped
public class FeatureEvoluationService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureDao featureDao;
    @Inject
    private FeatureEvoluationDao featureEvoluationDao;

    public FeatureEvoluation findLatest(long eventId) {
        return featureEvoluationDao.findLatest(eventId);
    }

    public List<FeatureEvoluation> findBetween(long eventId, DateTime from, DateTime to) {
        Event event = eventDao.findById(eventId);
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvoluationDao.findBetween(eventId, fromDate.toDate(), to.toDate());
    }

    @Transactional
    public void save(long eventId, List<Feature> features) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        for (Feature feature : features) {
            Feature createdFeature = featureDao.create(feature);
            featureEvoluationDao.create(createFeatureEvoluation(event, createdFeature, date));
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
