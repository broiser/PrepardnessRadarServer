package at.jku.cis.radar.dao;

import java.util.List;

import at.jku.cis.radar.modelv2.FeatureEntry;

public class FeatureEntryDao extends AbstractDao<FeatureEntry> {

	public FeatureEntryDao() {
		super(FeatureEntry.class);
	}
	
	public List<FeatureEntry> findFeaturesByEvent(long eventId){
		return createNamedQuery(FeatureEntry.FIND_FEATURE_BY_EVENT).setParameter("eventId", eventId).getResultList();
	}
	
	public FeatureEntry findFeatureByFeatureGroup(long featureGroup){
		return getSingleResult(createNamedQuery(FeatureEntry.FIND_FEATURE_BY_FEATURE_GROUP).setParameter("featureGroup", featureGroup).setMaxResults(1));
		
	}

}
