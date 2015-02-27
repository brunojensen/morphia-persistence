package org.mongodb.morphia.persistence;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

public class MorphiaProvider implements PersistenceProvider, ProviderUtil {

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(final PersistenceUnitInfo info,
                                                                    @SuppressWarnings("rawtypes") final Map map) {
        return new org.mongodb.morphia.persistence.EntityManagerFactory(new org.mongodb.morphia.persistence.util.PersistenceUnitInfo(info));
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(final String emName,
                                                           @SuppressWarnings("rawtypes") final Map map) {
        return new org.mongodb.morphia.persistence.EntityManagerFactory(new org.mongodb.morphia.persistence.util.PersistenceUnitInfo(emName));
    }

    @Override
    public ProviderUtil getProviderUtil() {
        return this;
    }

    @Override
    public LoadState isLoaded(final Object entity) {
        return null;
    }

    @Override
    public LoadState isLoadedWithoutReference(final Object entity, final String attributeName) {
        return null;
    }

    @Override
    public LoadState isLoadedWithReference(final Object entity, final String attributeName) {
        return null;
    }

}
