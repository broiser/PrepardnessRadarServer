package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.GeometryEvolutionEntryDao;
import at.jku.cis.radar.modelv2.GeometryEvolutionEntry;

public class GeometryEvolutionEntryService implements Serializable {

	@Inject
	private GeometryEvolutionEntryDao geometryEvolutionEntryDao;

	public List<GeometryEvolutionEntry> getGeometryEvolution(long eventId, DateTime fromDate, DateTime toDate) {
		return geometryEvolutionEntryDao.findEvolutionsByEvent(eventId, fromDate.toDate(), toDate.toDate());
	}

}
