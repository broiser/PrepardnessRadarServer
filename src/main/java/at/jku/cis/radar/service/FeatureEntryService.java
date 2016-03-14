package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.model.v2.Event;
import at.jku.cis.radar.model.v2.FeatureEntry;

@ApplicationScoped
public class FeatureEntryService implements Serializable {

    @Inject
    private EventDao eventDao;
    @Inject
    private FeatureEntryDao featureEntryDao;

    public List<FeatureEntry> getFeatureEntry(long eventId) {
        return featureEntryDao.findFeaturesByEvent(eventId);
    }

    public List<FeatureEntry> findAllByEvent(long eventId, DateTime from, DateTime to) {
        Event event = eventDao.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findAllByEvent(event, from, to);
    }

    private List<FeatureEntry> findAllByEvent(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEntryDao.findAllByEvent(event.getId(), fromDate.toDate(), to.toDate());
    }
}
