package at.jku.cis.radar.service.v2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.model.v2.Event;
import at.jku.cis.radar.model.v2.FeatureEntry;
import at.jku.cis.radar.model.v2.GeometryEvolutionEntry;

@ApplicationScoped
public class FeatureEntryService implements Serializable {

    @Inject
    private FeatureEntryDao featureEntryDao;
    @Inject
    private EventService eventService;
    @Inject
    private FeatureGroupService featureGroupService;
    @Inject
    private GeometryEvolutionEntryService geometryEvolutionEntryService;

    public List<FeatureEntry> getFeatureEntry(long eventId) {
        return featureEntryDao.findFeaturesByEvent(eventId);
    }

    public List<FeatureEntry> findAllByEvent(long eventId, DateTime from, DateTime to) {
        Event event = eventService.findById(eventId);
        if (event == null) {
            return Collections.emptyList();
        }
        return findAllByEvent(event, from, to);
    }

    private List<FeatureEntry> findAllByEvent(Event event, DateTime from, DateTime to) {
        DateTime fromDate = from.minus(event.getValidationPeriod());
        return featureEntryDao.findAllByEvent(event.getId(), fromDate.toDate(), to.toDate());
    }

    @Transactional
    public FeatureEntry create(long eventId, Geometry geometry, Map<String, Object> properties) {
        FeatureEntry featureEntry = new FeatureEntry();
        featureEntry.setEvent(eventService.findById(eventId));
        featureEntry.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
        featureEntry.setGeometryEvolutionEntries(Arrays.asList(createGeometryEvolutionEntry(geometry, properties)));
        return featureEntryDao.create(featureEntry);
    }

    private GeometryEvolutionEntry createGeometryEvolutionEntry(Geometry geometry, Map<String, Object> properties) {
        return geometryEvolutionEntryService.create(geometry, properties);
    }
}
