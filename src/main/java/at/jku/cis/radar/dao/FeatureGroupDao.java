package at.jku.cis.radar.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

import at.jku.cis.radar.model.Feature;

@ApplicationScoped
public class FeatureGroupDao extends AbstractDao<Feature> {

    public FeatureGroupDao() {
        super(Feature.class);
    }

    public Long findNextFeatureGroup() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Expression<Long> featureGroupExpression = criteriaQuery.from(Feature.class).get("featureGroup").as(Long.class);
        criteriaQuery.select(featureGroupExpression).orderBy(criteriaBuilder.desc(featureGroupExpression));
        return getSingleResult(getEntityManager().createQuery(criteriaQuery).setMaxResults(1));
    }

}
