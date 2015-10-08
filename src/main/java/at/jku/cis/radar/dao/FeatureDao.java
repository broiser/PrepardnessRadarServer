package at.jku.cis.radar.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

import at.jku.cis.radar.model.Feature;

@ApplicationScoped
public class FeatureDao extends AbstractDao<Feature> {

    public FeatureDao() {
        super(Feature.class);
    }

    public Feature findByReference(String featureReference) {
        TypedQuery<Feature> typedQuery = createNamedQuery(Feature.FIND_BY_REFERENCE);
        typedQuery.setParameter("featureReference", featureReference);
        return getSingleResult(typedQuery);
    }

}
