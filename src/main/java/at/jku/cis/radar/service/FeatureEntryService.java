package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;

import at.jku.cis.radar.dao.FeatureEntryDao;
import at.jku.cis.radar.modelv2.FeatureEntry;

public class FeatureEntryService implements Serializable {

	@Inject
	private FeatureEntryDao featureEntryDao;

	public List<FeatureEntry> getFeatureEntry(long eventId) {
		return featureEntryDao.findFeaturesByEvent(eventId);
	}

}
