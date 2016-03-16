package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.model.Event;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;

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
	public FeatureEntry create(long eventId, Geometry geometry, Map<String, Object> properties) {
		FeatureEntry featureEntry = new FeatureEntry();
		featureEntry.setEvent(eventService.findById(eventId));
		featureEntry.setFeatureGroup(featureGroupService.generateNextFeatureGroup());
		featureEntry.setGeometryEvolutionEntries(new ArrayList<GeometryEvolutionEntry>());
		featureEntry = featureEntryDao.create(featureEntry);
		featureEntry.getGeometryEvolutionEntries()
				.add(createGeometryEvolutionEntry(featureEntry, geometry, properties));
		return featureEntryDao.save(featureEntry);
	}

	@Transactional
	public FeatureEntry evolve(long featureGroup, Geometry geometry, Map<String, Object> properties) {
		FeatureEntry featureEntry = featureEntryDao.findByFeatureGroup(featureGroup);
		featureEntry.getGeometryEvolutionEntries()
				.add(createGeometryEvolutionEntry(featureEntry, geometry, properties));
		return featureEntryDao.save(featureEntry);
	}

	@Transactional
	public FeatureEntry edit(long featureGroup, Geometry geometry, Map<String, Object> properties) {
		FeatureEntry featureEntry = featureEntryDao.findByFeatureGroup(featureGroup);
		GeometryEvolutionEntry geometryEvolutionEntry = featureEntry.getGeometryEvolutionEntries()
				.remove(featureEntry.getGeometryEvolutionEntries().size() - 1);
		featureEntry.getGeometryEvolutionEntries()
				.add(editGeometryEvolutionEntry(geometryEvolutionEntry.getId(), geometry, properties));
		return featureEntryDao.save(featureEntry);
	}

	private GeometryEvolutionEntry editGeometryEvolutionEntry(long geometryEvolutionId, Geometry geometry,
			Map<String, Object> properties) {
		return geometryEvolutionEntryService.edit(geometryEvolutionId, geometry, properties);
	}

	private GeometryEvolutionEntry createGeometryEvolutionEntry(FeatureEntry featureEntry, Geometry geometry,
			Map<String, Object> properties) {
		return geometryEvolutionEntryService.create(featureEntry, geometry, properties);
	}
}
