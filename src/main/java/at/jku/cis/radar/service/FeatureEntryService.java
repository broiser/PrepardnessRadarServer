package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;
import at.jku.cis.radar.model.GeometryStatus;

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

    public FeatureEntry findByFeatureGroup(long featureGroup) {
        return featureEntryDao.findByFeatureGroup(featureGroup);
    }

    @Transactional
    public FeatureEntry create(long eventId, String username, GeometryStatus geometryStatus, Geometry geometry) {
        FeatureEntry featureEntry = new FeatureEntry();
        featureEntry.setEvent(eventService.findById(eventId));
        featureEntry.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
        featureEntry.setGeometryEvolutionEntries(new ArrayList<GeometryEvolutionEntry>());
        featureEntry = featureEntryDao.create(featureEntry);
        featureEntry.getGeometryEvolutionEntries()
                .add(createGeometryEvolutionEntry(featureEntry, username, geometryStatus, geometry));
        return featureEntryDao.save(featureEntry);
    }

    @Transactional
    public FeatureEntry evolve(long featureGroup, String username, GeometryStatus geometryStatus, Geometry geometry) {
        FeatureEntry featureEntry = featureEntryDao.findByFeatureGroup(featureGroup);
        featureEntry.getGeometryEvolutionEntries()
                .add(createGeometryEvolutionEntry(featureEntry, username, geometryStatus, geometry));
        return featureEntryDao.save(featureEntry);
    }

    @Transactional
    public FeatureEntry edit(long featureGroup, GeometryStatus geometryStatus, Geometry geometry) {
        FeatureEntry featureEntry = featureEntryDao.findByFeatureGroup(featureGroup);
        GeometryEvolutionEntry geometryEvolutionEntry = featureEntry.getGeometryEvolutionEntries()
                .remove(featureEntry.getGeometryEvolutionEntries().size() - 1);
        featureEntry.getGeometryEvolutionEntries()
                .add(editGeometryEvolutionEntry(geometryEvolutionEntry.getId(), geometryStatus, geometry));
        return featureEntryDao.save(featureEntry);
    }

    @Transactional
    public FeatureEntry updateFeatureContent(long featureGroup, String title, String description) {
        FeatureEntry featureEntry = findByFeatureGroup(featureGroup);
        featureEntry.setTitle(title);
        featureEntry.setDescription(description);
        return featureEntryDao.save(featureEntry);
    }

    @Transactional
    public void updateDescription(long featureGroup, String description) {
        FeatureEntry featureEntry = findByFeatureGroup(featureGroup);
        featureEntry.setDescription(description);
        featureEntryDao.save(featureEntry);
    }

    private GeometryEvolutionEntry createGeometryEvolutionEntry(FeatureEntry featureEntry, String username,
            GeometryStatus geometryStatus, Geometry geometry) {
        GeometryEvolutionEntry geometryEvolutionEntry = geometryEvolutionEntryService.create(username, geometryStatus,
                geometry);
        geometryEvolutionEntry.setFeatureEntry(featureEntry);
        return geometryEvolutionEntry;
    }

    private GeometryEvolutionEntry editGeometryEvolutionEntry(long geometryEvolutionId, GeometryStatus geometryStatus,
            Geometry geometry) {
        return geometryEvolutionEntryService.edit(geometryEvolutionId, geometryStatus, geometry);
    }
}
