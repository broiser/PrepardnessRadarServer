package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvoluationService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureService featureService;
    @Inject
    private FeatureEvoluationDao featureEvoluationDao;

    public List<FeatureEvolution> findBetween(long eventId, DateTime from, DateTime to) {
        Event event = eventDao.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findBetween(event, from, to);
    }

    private List<FeatureEvolution> findBetween(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvoluationDao.findBetween(event.getId(), fromDate.toDate(), to.toDate());
    }

    public FeatureEvolution findLatestByReference(long eventId, String featureReference) {
        return featureEvoluationDao.findLatestByReference(eventId, featureReference);
    }

    @Transactional
    public FeatureEvolution update(long eventId, Feature feature) {
        FeatureEvolution featureEvoluation = findLatestByReference(eventId, feature.getFeatureGroup());
        if (featureEvoluation == null) {
            throw new IllegalArgumentException("FeatureReference not found");
        }
        Feature currentFeature = featureEvoluation.getFeature();
        currentFeature.setGeometry(feature.getGeometry());
        currentFeature.setProperties(feature.getProperties());
        featureEvoluation.setFeature(featureService.save(currentFeature));
        return featureEvoluationDao.save(featureEvoluation);
    }

    @Transactional
    public FeatureEvolution save(long eventId, Feature feature) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        return saveFeatureEvoluation(feature, event, date);
    }

    @Transactional
    public List<FeatureEvolution> save(long eventId, List<Feature> features) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        List<FeatureEvolution> featureEvoluations = new ArrayList<>();
        for (Feature feature : features) {
            featureEvoluations.add(saveFeatureEvoluation(feature, event, date));
        }
        return featureEvoluations;
    }

    private FeatureEvolution saveFeatureEvoluation(Feature feature, Event event, Date date) {
        Feature createdFeature = featureService.save(feature);
        return featureEvoluationDao.create(createFeatureEvoluation(event, createdFeature, date));
    }

    private FeatureEvolution createFeatureEvoluation(Event event, Feature feature, Date date) {
        FeatureEvolution featureEvoluation = new FeatureEvolution();
        featureEvoluation.setDate(date);
        featureEvoluation.setEvent(event);
        featureEvoluation.setFeature(feature);
        return featureEvoluation;
    }
}
