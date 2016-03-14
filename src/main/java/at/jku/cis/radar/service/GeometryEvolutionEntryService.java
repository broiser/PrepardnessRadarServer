package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.GeometryEvolutionEntryDao;
import at.jku.cis.radar.modelv2.GeometryEvolutionEntry;


@ApplicationScoped
public class GeometryEvolutionEntryService implements Serializable {

	@Inject
	private GeometryEvolutionEntryDao geometryEvolutionEntryDao;

	public List<GeometryEvolutionEntry> getGeometryEvolution(long eventId, DateTime fromDate, DateTime toDate) {
		return geometryEvolutionEntryDao.findEvolutionsByEvent(eventId, fromDate.toDate(), toDate.toDate());
	}

	@Transactional
	public GeometryEvolutionEntry save(GeometryEvolutionEntry geometryEvolutionEntry) {
		return geometryEvolutionEntryDao.save(geometryEvolutionEntry);
	}
	
	@Transactional
	public GeometryEvolutionEntry create(GeometryEvolutionEntry geometryEvolutionEntry) {
		return geometryEvolutionEntryDao.create(geometryEvolutionEntry);
	}

}
