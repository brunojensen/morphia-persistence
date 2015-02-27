package org.mongodb.morphia.persistence.util;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

public class PersistenceUnitInfo implements javax.persistence.spi.PersistenceUnitInfo {

    private final javax.persistence.spi.PersistenceUnitInfo info;
    private final String unitName;

    public PersistenceUnitInfo(final javax.persistence.spi.PersistenceUnitInfo info) {
        unitName = info.getPersistenceUnitName();
        this.info = info;
    }

    public PersistenceUnitInfo(final String unitName) {
        this.unitName = unitName;
        info = null;
    }

    @Override
    public void addTransformer(final ClassTransformer transformer) {
        info.addTransformer(transformer);
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return info.excludeUnlistedClasses();
    }

    @Override
    public ClassLoader getClassLoader() {
        return info.getClassLoader();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return info.getJarFileUrls();
    }

    @Override
    public DataSource getJtaDataSource() {
        return info.getJtaDataSource();
    }

    @Override
    public List<String> getManagedClassNames() {
        return info.getManagedClassNames();
    }

    @Override
    public List<String> getMappingFileNames() {
        return info.getManagedClassNames();
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return info.getNewTempClassLoader();
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return info.getNonJtaDataSource();
    }

    @Override
    public String getPersistenceProviderClassName() {
        return info.getPersistenceProviderClassName();
    }

    @Override
    public String getPersistenceUnitName() {
        return unitName;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return info.getPersistenceUnitRootUrl();
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return info.getPersistenceXMLSchemaVersion();
    }

    @Override
    public Properties getProperties() {
        return info.getProperties();
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return info.getSharedCacheMode();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return info.getTransactionType();
    }

    @Override
    public ValidationMode getValidationMode() {
        return info.getValidationMode();
    }

    public boolean isLoaded() {
        return null != info;
    }
}
