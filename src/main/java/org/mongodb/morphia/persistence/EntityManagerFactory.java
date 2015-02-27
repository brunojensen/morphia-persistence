package org.mongodb.morphia.persistence;

import java.util.Collections;
import java.util.Map;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import org.mongodb.morphia.persistence.util.PersistenceUnitInfo;

public class EntityManagerFactory implements javax.persistence.EntityManagerFactory {

    private org.mongodb.morphia.persistence.EntityManager entityManager;
    private final PersistenceUnitInfo info;

    public EntityManagerFactory(final PersistenceUnitInfo info) {
        this.info = info;
    }

    @Override
    public void close() {
        createEntityManager().close();
    }

    @Override
    public EntityManager createEntityManager() {
        return createEntityManager(Collections.emptyMap());
    }

    @Override
    public EntityManager createEntityManager(@SuppressWarnings("rawtypes") final Map map) {
        if (null == entityManager || !entityManager.isOpen()) {
            entityManager = new org.mongodb.morphia.persistence.EntityManager(this);
        }
        return entityManager;
    }

    @Override
    public Cache getCache() {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return createEntityManager().getProperties();
    }

    @Override
    public boolean isOpen() {
        return createEntityManager().isOpen();
    }

    public PersistenceUnitInfo persistenceUnitInfo() {
        return info;
    }
}
