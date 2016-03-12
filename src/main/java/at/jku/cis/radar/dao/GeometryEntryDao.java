package at.jku.cis.radar.dao;

import java.util.List;

import at.jku.cis.radar.modelv2.GeometryEntry;

public class GeometryEntryDao extends AbstractDao<GeometryEntry> {

	public GeometryEntryDao() {
		super(GeometryEntry.class);
	}

	public List<GeometryEntry> findGeometriesByEvolutionId(long evolutionId) {
		return createNamedQuery(GeometryEntry.FIND_GEOMETRIES_BY_EVOLUTION_ID).setParameter("evolutionId", evolutionId)
				.getResultList();
	}

}
