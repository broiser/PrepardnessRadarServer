package at.jku.cis.radar.dao;

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

}
