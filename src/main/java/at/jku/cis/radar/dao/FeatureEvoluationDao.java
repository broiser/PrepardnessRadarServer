package at.jku.cis.radar.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.FeatureEvoluation;

@ApplicationScoped
public class FeatureEvoluationDao extends AbstractDao<FeatureEvoluation> {

    public FeatureEvoluationDao() {
        super(FeatureEvoluation.class);
    }

    public FeatureEvoluation findLatest(long eventId) {
        return getSingleResult(createNamedQuery(FeatureEvoluation.FIND_LATEST_BY_EVENT).setParameter("eventId", eventId)
                .setMaxResults(1));
    }

    public List<FeatureEvoluation> findBetween(long eventId, Date fromDate, Date toDate) {
        return createNamedQuery(FeatureEvoluation.FIND_BETWEEN_BY_EVENT).setParameter("eventId", eventId)
                .setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
    }
}
