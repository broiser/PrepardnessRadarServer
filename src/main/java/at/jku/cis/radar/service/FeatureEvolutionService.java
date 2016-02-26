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
import org.joda.time.Period;

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

    private final long EDIT_ORDER_INITIAL_VALUE = 1;
    
    
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
        
        Event event = eventService.findById(eventId);
        geoJsonFeature.setFeatureGroup(featureEvolution.getFeatureGroup());
        return createFeatureEvolution(geoJsonFeature, event, featureEvolution.getDate(), getNextEditOrder(featureEvolution));
    }

	private long getNextEditOrder(FeatureEvolution featureEvolution) {
		return featureEvolution.getEditorder() + 1;
	}

    @Transactional
    public FeatureEvolution create(long eventId, GeoJsonFeature geoJsonFeature) {
        Event event = eventService.findById(eventId);
        Date date = DateTime.now().toDate();
        return createFeatureEvolution(geoJsonFeature, event, date, EDIT_ORDER_INITIAL_VALUE);
    }

    @Transactional
    public List<FeatureEvolution> save(long eventId, List<GeoJsonFeature> geoJsonFeatures) {
        Event event = eventService.findById(eventId);
        Date date = DateTime.now().toDate();
        List<FeatureEvolution> featureEvolutions = new ArrayList<>();
        for (GeoJsonFeature geoJsonFeature : geoJsonFeatures) {
            featureEvolutions.add(createFeatureEvolution(geoJsonFeature, event, date, 1));
        }
        return featureEvolutions;
    }

    private FeatureEvolution findNewestByFeatureGroup(long eventId, long featureGroup) {
        return featureEvolutionDao.findNewestByFeatureGroup(eventId, featureGroup);
    }

    private FeatureEvolution createFeatureEvolution(GeoJsonFeature geoJsonFeature, Event event, Date date, long editorder) {
        FeatureEvolution featureEvolution = buildFeatureEvolution(geoJsonFeature, event, date, editorder);
        return featureEvolutionDao.create(featureEvolution);
    }

    private FeatureEvolution buildFeatureEvolution(GeoJsonFeature geoJsonFeature, Event event, Date date, long editorder) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setEvent(event);
        featureEvolution.setGeometry(geoJsonFeature.getGeometry());
        featureEvolution.setProperties(geoJsonFeature.getProperties());
        featureEvolution.setStatus(geoJsonFeature.getProperties().get("STATUS").equals("ERASED") ? 'e' : 'c');
        featureEvolution.setEditorder(editorder);
        if (geoJsonFeature.getFeatureGroup() <= 0) {
            featureEvolution.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
        } else {
            featureEvolution.setFeatureGroup(geoJsonFeature.getFeatureGroup());
        }
        return featureEvolution;
    }

}
