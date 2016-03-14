package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.modelv2.FeatureEntry;


@ApplicationScoped
public class FeatureEntryService implements Serializable {

	@Inject
	private FeatureEntryDao featureEntryDao;

	public List<FeatureEntry> getFeatureEntry(long eventId) {
		return featureEntryDao.findFeaturesByEvent(eventId);
	}
	
	
	@Transactional
	public FeatureEntry save(FeatureEntry featureEntry){
		return featureEntryDao.save(featureEntry);
	}
	
	@Transactional
	public FeatureEntry create(FeatureEntry featureEntry){
		return featureEntryDao.create(featureEntry);
	}

}
