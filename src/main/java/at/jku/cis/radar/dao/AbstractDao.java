package at.jku.cis.radar.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import at.jku.cis.radar.model.v2.BaseEntity;

public class AbstractDao<T extends BaseEntity> implements Serializable {

    @Inject
    private EntityManager entityManager;

    private Class<T> entityClass;

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(long id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = entityManager.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional(value = TxType.MANDATORY)
    public T save(T entity) {
        return entityManager.merge(entity);
    }

    @Transactional(value = TxType.MANDATORY)
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected TypedQuery<T> createNamedQuery(String queryName) {
        return createNamedQuery(queryName, entityClass);
    }

    protected <G> TypedQuery<G> createNamedQuery(String queryName, Class<G> clazz) {
        return entityManager.createNamedQuery(queryName, clazz);
    }

    protected <G> G getSingleResult(TypedQuery<G> typedQuery) {
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}