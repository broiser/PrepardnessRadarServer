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

import at.jku.cis.radar.dao.FeatureEvolutionDao;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionService implements Serializable {

    @Inject
    private EventService eventService;
    @Inject
    private FeatureGroupService featureGroupService;
    @Inject
    private FeatureEvolutionDao featureEvolutionDao;

    public List<FeatureEvolution> findNewestByEvent(long eventId, DateTime from, DateTime to) {
        Event event = eventService.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findNewestByEvent(event, from, to);
    }

    private List<FeatureEvolution> findNewestByEvent(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvolutionDao.findNewestByEvent(event.getId(), fromDate.toDate(), to.toDate());
    }

    public List<FeatureEvolution> findBetween(long eventId, long featureGroup, DateTime from, DateTime to) {
        Event event = eventService.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findBetween(event, featureGroup, from, to);
    }

    private List<FeatureEvolution> findBetween(Event event, long featureGroup, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEvolutionDao.findBetween(event.getId(), featureGroup, fromDate.toDate(), to.toDate());
    }

    @Transactional
    public FeatureEvolution save(long eventId, GeoJsonFeature geoJsonFeature) {
        FeatureEvolution featureEvolution = findNewestByFeatureGroup(eventId, geoJsonFeature.getFeatureGroup());
        if (featureEvolution == null) {
            throw new IllegalArgumentException("FeatureGroup not found");
        }
        featureEvolution.setGeometry(geoJsonFeature.getGeometry());
        featureEvolution.setProperties(geoJsonFeature.getProperties());
        return featureEvolutionDao.save(featureEvolution);
    }

    @Transactional
    public FeatureEvolution create(long eventId, GeoJsonFeature geoJsonFeature) {
        Event event = eventService.findById(eventId);
        Date date = DateTime.now().toDate();
        return createFeatureEvolution(geoJsonFeature, event, date);
    }

    @Transactional
    public List<FeatureEvolution> save(long eventId, List<GeoJsonFeature> geoJsonFeatures) {
        Event event = eventService.findById(eventId);
        Date date = DateTime.now().toDate();
        List<FeatureEvolution> featureEvolutions = new ArrayList<>();
        for (GeoJsonFeature geoJsonFeature : geoJsonFeatures) {
            featureEvolutions.add(createFeatureEvolution(geoJsonFeature, event, date));
        }
        return featureEvolutions;
    }

    private FeatureEvolution findNewestByFeatureGroup(long eventId, long featureGroup) {
        return featureEvolutionDao.findNewestByFeatureGroup(eventId, featureGroup);
    }

    private FeatureEvolution createFeatureEvolution(GeoJsonFeature geoJsonFeature, Event event, Date date) {
        FeatureEvolution featureEvolution = buildFeatureEvolution(geoJsonFeature, event, date);
        return featureEvolutionDao.create(featureEvolution);
    }

    private FeatureEvolution buildFeatureEvolution(GeoJsonFeature geoJsonFeature, Event event, Date date) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setEvent(event);
        featureEvolution.setGeometry(geoJsonFeature.getGeometry());
        featureEvolution.setProperties(geoJsonFeature.getProperties());
        if (geoJsonFeature.getFeatureGroup() <= 0) {
            featureEvolution.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
        } else {
            featureEvolution.setFeatureGroup(geoJsonFeature.getFeatureGroup());
        }
        return featureEvolution;
    }

}
