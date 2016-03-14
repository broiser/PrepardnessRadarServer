package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import at.jku.cis.radar.dao.GeometryEntryDao;
import at.jku.cis.radar.modelv2.GeometryEntry;


@ApplicationScoped
public class GeometryEntryService implements Serializable {

	@Inject
	private GeometryEntryDao geometryEntryDao;

	public List<GeometryEntry> getGeometryEntry(long geometryEvolutionId) {
		return geometryEntryDao.findGeometriesByEvolutionId(geometryEvolutionId);
	}

	@Transactional
	public GeometryEntry save(GeometryEntry geometryEntry) {
		return geometryEntryDao.save(geometryEntry);
	}

	@Transactional
	public GeometryEntry create(GeometryEntry geometryEntry) {
		return geometryEntryDao.create(geometryEntry);
	}
	
}
