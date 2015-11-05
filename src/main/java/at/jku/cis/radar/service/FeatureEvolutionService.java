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
import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureGroupService featureGroupService;
    @Inject
    private FeatureEvolutionDao featureEvolutionDao;

    public List<FeatureEvolution> findNewestByEvent(long eventId, DateTime from, DateTime to) {
        Event event = eventDao.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findNewestByEvent(event, from, to);
    }

    private List<FeatureEvolution> findNewestByEvent(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvolutionDao.findNewestByEvent(event.getId(), fromDate.toDate(), to.toDate());
    }

    public List<FeatureEvolution> findBetween(long eventId, long featureGroup, DateTime fromDate, DateTime toDate) {
        return featureEvolutionDao.findBetween(eventId, featureGroup, fromDate.toDate(), toDate.toDate());
    }

    @Transactional
    public FeatureEvolution save(long eventId, Feature feature) {
        FeatureEvolution featureEvolution = findNewestByFeatureGroup(eventId, feature.getFeatureGroup());
        if (featureEvolution == null) {
            throw new IllegalArgumentException("FeatureGroup not found");
        }
        featureEvolution.setGeometry(feature.getGeometry());
        featureEvolution.setProperties(feature.getProperties());
        return featureEvolutionDao.save(featureEvolution);
    }

    @Transactional
    public FeatureEvolution create(long eventId, Feature feature) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        return createFeatureEvolution(feature, event, date);
    }

    @Transactional
    public List<FeatureEvolution> save(long eventId, List<Feature> features) {
        Event event = eventDao.findById(eventId);
        Date date = DateTime.now().toDate();
        List<FeatureEvolution> featureEvolutions = new ArrayList<>();
        for (Feature feature : features) {
            featureEvolutions.add(createFeatureEvolution(feature, event, date));
        }
        return featureEvolutions;
    }

    private FeatureEvolution findNewestByFeatureGroup(long eventId, long featureGroup) {
        return featureEvolutionDao.findNewestByFeatureGroup(eventId, featureGroup);
    }

    private FeatureEvolution createFeatureEvolution(Feature feature, Event event, Date date) {
        FeatureEvolution featureEvolution = buildFeatureEvolution(feature, event, date);
        return featureEvolutionDao.create(featureEvolution);
    }

    private FeatureEvolution buildFeatureEvolution(Feature feature, Event event, Date date) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setEvent(event);
        featureEvolution.setGeometry(feature.getGeometry());
        featureEvolution.setProperties(feature.getProperties());
        if (feature.getFeatureGroup() <= 0) {
            featureEvolution.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
        } else{
        	featureEvolution.setFeatureGroup(feature.getFeatureGroup());
        }
        return featureEvolution;
    }

}
