package at.jku.cis.radar.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionDao extends AbstractDao<FeatureEvolution> {

    public FeatureEvolutionDao() {
        super(FeatureEvolution.class);
    }

    public List<FeatureEvolution> findNewestByEvent(long eventId, Date fromDate, Date toDate) {
        return createNamedQuery(FeatureEvolution.FIND_NEWEST_BY_EVENT).setParameter("eventId", eventId)
                .setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
    }

    public FeatureEvolution findNewestByFeatureGroup(long eventId, long featureGroup) {
        return getSingleResult(createNamedQuery(FeatureEvolution.FIND_NEWEST_BY_FEATURE_GROUP)
                .setParameter("eventId", eventId).setParameter("featureGroup", featureGroup).setMaxResults(1));
    }

    public List<FeatureEvolution> findBetween(long eventId, long featureGroup, Date fromDate, Date toDate) {
        return createNamedQuery(FeatureEvolution.FIND_BETWEEN_BY_EVENT_AND_FEATURE_GROUP)
                .setParameter("eventId", eventId).setParameter("featureGroup", featureGroup)
                .setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();

    }
}
