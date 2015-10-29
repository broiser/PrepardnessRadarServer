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
import at.jku.cis.radar.dao.FeatureEvolutionDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.Feature;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureService featureService;
    @Inject
    private FeatureEvolutionDao featureEvolutionDao;

    public List<FeatureEvolution> findBetween(long eventId, DateTime from, DateTime to) {
        Event event = eventDao.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findBetween(event, from, to);
    }

    private List<FeatureEvolution> findBetween(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvolutionDao.findBetween(event.getId(), fromDate.toDate(), to.toDate());
    }

    public List<FeatureEvolution> findBetween(long eventId, long featureGroup, DateTime fromDate, DateTime toDate) {
        return featureEvolutionDao.findBetween(eventId, featureGroup, fromDate.toDate(), toDate.toDate());
    }

    @Transactional
    public FeatureEvolution update(long eventId, Feature feature) {
        FeatureEvolution featureEvolution = findNewestByFeatureGroup(eventId, feature.getFeatureGroup());
        if (featureEvolution == null) {
            throw new IllegalArgumentException("FeatureGroup not found");
        }
        Feature currentFeature = featureEvolution.getFeature();
        currentFeature.setGeometry(feature.getGeometry());
        currentFeature.setProperties(feature.getProperties());
        featureEvolution.setFeature(featureService.save(currentFeature));
        return featureEvolutionDao.save(featureEvolution);
    }

    @Transactional
    public FeatureEvolution save(long eventId, Feature feature) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        return saveFeatureEvolution(feature, event, date);
    }

    @Transactional
    public List<FeatureEvolution> save(long eventId, List<Feature> features) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        List<FeatureEvolution> featureEvolutions = new ArrayList<>();
        for (Feature feature : features) {
            featureEvolutions.add(saveFeatureEvolution(feature, event, date));
        }
        return featureEvolutions;
    }

    private FeatureEvolution findNewestByFeatureGroup(long eventId, long featureGroup) {
        return featureEvolutionDao.findNewestByFeatureGroup(eventId, featureGroup);
    }

    private FeatureEvolution saveFeatureEvolution(Feature feature, Event event, Date date) {
        Feature createdFeature = featureService.save(feature);
        return featureEvolutionDao.create(createFeatureEvolution(event, createdFeature, date));
    }

    private FeatureEvolution createFeatureEvolution(Event event, Feature feature, Date date) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setEvent(event);
        featureEvolution.setFeature(feature);
        return featureEvolution;
    }

}
