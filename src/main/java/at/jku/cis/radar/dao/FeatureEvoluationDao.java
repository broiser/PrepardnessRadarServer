package at.jku.cis.radar.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvoluationDao extends AbstractDao<FeatureEvolution> {

    public FeatureEvoluationDao() {
        super(FeatureEvolution.class);
    }

    public List<FeatureEvolution> findBetween(long eventId, Date fromDate, Date toDate) {
        return createNamedQuery(FeatureEvolution.FIND_BETWEEN_BY_EVENT).setParameter("eventId", eventId)
                .setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
    }

    public FeatureEvolution findLatestByReference(long eventId, long featureGroup) {
        return getSingleResult(createNamedQuery(FeatureEvolution.FIND_LATEST_BY_REFERENCE)
                .setParameter("eventId", eventId).setParameter("featureReference", featureGroup).setMaxResults(1));
    }
}
