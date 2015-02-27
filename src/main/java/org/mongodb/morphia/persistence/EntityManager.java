package org.mongodb.morphia.persistence;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.MongoClient;

public class EntityManager implements javax.persistence.EntityManager {

    private final Datastore db;
    private final EntityManagerFactory factory;
    private boolean open;

    public EntityManager(final org.mongodb.morphia.persistence.EntityManagerFactory factory) {
        final PersistenceUnit persistenceUnit = PersistenceUnit.getInstance(factory.persistenceUnitInfo());
        db = createDatastore(persistenceUnit, createClient(persistenceUnit));
        this.factory = factory;

    }

    @Override
    public void clear() {
    }

    @Override
    public void close() {
        synchronized (this) {
            db.getMongo()
              .close();
            open = false;
        }
    }

    @Override
    public boolean contains(final Object entity) {
        return false;
    }

    private MongoClient createClient(final PersistenceUnit persistenceUnit) throws PersistenceException {
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(persistenceUnit.host(), persistenceUnit.port());
        } catch (final UnknownHostException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return mongoClient;
    }

    private Datastore createDatastore(final PersistenceUnit persistenceUnit, final MongoClient mongoClient)
        throws PersistenceException {
        final Morphia morphia = new Morphia();
        final Datastore db = morphia.createDatastore(mongoClient, persistenceUnit.database());
        if (null != persistenceUnit.mapPackage()) {
            morphia.mapPackage(persistenceUnit.mapPackage(),
                               null == persistenceUnit.mapPackage() ? true : persistenceUnit.ignoreInvalid());
        } else if (null != persistenceUnit.classes() && !persistenceUnit.classes()
            .isEmpty()) {
            for (final String clazz : persistenceUnit.classes()) {
                try {
                    morphia.map(Class.forName(clazz));
                } catch (final ClassNotFoundException e) {
                    throw new PersistenceException(e.getMessage(), e);
                }
            }
        }
        return db;
    }

    @Override
    public Query createNamedQuery(final String name) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(final String name, final Class<T> resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(final String sqlString) {
        return null;
    }

    @Override
    public Query createNativeQuery(final String sqlString, @SuppressWarnings("rawtypes") final Class resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(final String sqlString, final String resultSetMapping) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
        return null;
    }

    @Override
    public Query createQuery(final String qlString) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(final String qlString, final Class<T> resultClass) {
        return null;
    }

    @Override
    public void detach(final Object entity) {
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        return db.get(entityClass, primaryKey);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode) {
        return db.get(entityClass, primaryKey);
    }

    @Override
    public <T> T find(final Class<T> entityClass,
                      final Object primaryKey,
                      final LockModeType lockMode,
                      final Map<String, Object> properties) {
        return db.get(entityClass, primaryKey);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final Map<String, Object> properties) {
        return db.get(entityClass, primaryKey);
    }

    @Override
    public void flush() {
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Object getDelegate() {
        return db;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public LockModeType getLockMode(final Object entity) {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }

    @Override
    public <T> T getReference(final Class<T> entityClass, final Object primaryKey) {
        return find(entityClass, primaryKey);
    }

    @Override
    public EntityTransaction getTransaction() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void joinTransaction() {
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode) {
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
    }

    @Override
    public <T> T merge(final T entity) {
        db.save(entity);
        return entity;
    }

    @Override
    public void persist(final Object entity) {
        merge(entity);
    }

    @Override
    public void refresh(final Object entity) {
        merge(entity);
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode) {
        merge(entity);
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
        merge(entity);
    }

    @Override
    public void refresh(final Object entity, final Map<String, Object> properties) {
        merge(entity);
    }

    @Override
    public void remove(final Object entity) {
        db.delete(entity);
    }

    @Override
    public void setFlushMode(final FlushModeType flushMode) {
    }

    @Override
    public void setProperty(final String propertyName, final Object value) {

    }

    @Override
    public <T> T unwrap(final Class<T> cls) {
        return null;
    }

}
