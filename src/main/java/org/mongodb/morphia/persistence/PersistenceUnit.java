package org.mongodb.morphia.persistence;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.JAXBContext;
import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.persistence.util.Base64;
import org.mongodb.morphia.persistence.util.PersistenceUnitInfo;
import org.mongodb.morphia.persistence.util.PropertyResolver;
import com.sun.java.xml.ns.persistence.Persistence;
import com.sun.java.xml.ns.persistence.Persistence.PersistenceUnit.Properties;

final class PersistenceUnit implements Serializable {

    private static final Logger LOGGER = MorphiaLoggerFactory.get(PersistenceUnit.class);
    private static final String MONGODB_DATABASE_HOST = "mongodb.database.host";
    private static final String MONGODB_DATABASE_NAME = "mongodb.database.name";
    private static final String MONGODB_DATABASE_PASSWORD = "mongodb.database.password";
    private static final String MONGODB_DATABASE_PORT = "mongodb.database.port";
    private static final String MONGODB_DATABASE_USERNAME = "mongodb.database.username";
    private static final String MONGODB_MORPHIA_IGNORE_INVALID = "mongodb.morphia.ignore_invalid";
    private static final String MONGODB_MORPHIA_PACKAGE = "mongodb.morphia.package";
    private static final String PERSISTENCE_XML_PATH = String.format("META-INF%spersistence.xml", File.separator);

    private static final long serialVersionUID = 8052176776795617867L;

    static PersistenceUnit getInstance(final PersistenceUnitInfo info) {
        if (info.isLoaded()) {
            return new PersistenceUnit(info);
        }
        final PersistenceUnit unit = new PersistenceUnit(info.getPersistenceUnitName());
        try {
            unit.check();
        } catch (final Exception e) {
            LOGGER.error("Persistence unit not loaded.", e);
            throw new RuntimeException(e);
        }
        return unit;
    }

    private Persistence.PersistenceUnit persistence;

    private final java.util.Properties properties = new java.util.Properties();

    private final String unitName;

    private PersistenceUnit(final PersistenceUnitInfo info) {
        unitName = info.getPersistenceUnitName();
        persistence = new Persistence.PersistenceUnit();
        persistence.setProvider(info.getPersistenceProviderClassName());
        persistence.setName(info.getPersistenceUnitName());
        persistence.getClazz()
        .addAll(info.getManagedClassNames());
        properties.putAll(info.getProperties());
    }

    private PersistenceUnit(final String unitName) {
        this.unitName = unitName;
    }

    private final boolean check() throws Exception {
        return null == persistence ? load() : false;
    }

    final List<String> classes() {
        try {
            check();
            return persistence.getClazz();
        } catch (final Exception e) {
            LOGGER.error("Persistence unit not loaded.", e);
            throw new RuntimeException(e);
        }
    }

    String database() {
        return PropertyResolver.resolve(properties.getProperty(MONGODB_DATABASE_NAME));
    }

    String host() {
        return PropertyResolver.resolve(properties.getProperty(MONGODB_DATABASE_HOST));
    }

    boolean ignoreInvalid() {
        return Boolean.valueOf(PropertyResolver.resolve(properties.getProperty(MONGODB_MORPHIA_IGNORE_INVALID)));
    }

    private boolean load() throws Exception {
        final ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
        final List<Persistence.PersistenceUnit> elements = ((Persistence) JAXBContext.newInstance(Persistence.class)
            .createUnmarshaller()
            .unmarshal(classLoader.getResourceAsStream(PERSISTENCE_XML_PATH))).getPersistenceUnit();
        loadPersistenceUnit(elements);
        loadProperties();
        return true;
    }

    private void loadPersistenceUnit(final List<Persistence.PersistenceUnit> elements) {
        for (final Persistence.PersistenceUnit element : elements) {
            if (unitName.equalsIgnoreCase(element.getName())) {
                persistence = element;
                break;
            }
        }
        if (null == persistence) {
            final String msg = "Couldn't read persistence.xml properly;";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
    }

    private void loadProperties() {
        for (final Properties.Property property : properties().getProperty()) {
            properties.put(property.getName(), property.getValue());
        }
    }

    String mapPackage() {
        return PropertyResolver.resolve(properties.getProperty(MONGODB_MORPHIA_PACKAGE));
    }

    String password() {
        return Base64.decode(PropertyResolver.resolve(properties.getProperty(MONGODB_DATABASE_PASSWORD)));
    }

    int port() {
        return Integer.valueOf(PropertyResolver.resolve(properties.getProperty(MONGODB_DATABASE_PORT)));
    }

    final Properties properties() {
        try {
            check();
            return persistence.getProperties();
        } catch (final Exception e) {
            LOGGER.error("Persistence unit not loaded.", e);
            throw new RuntimeException(e);
        }
    }

    String userName() {
        return PropertyResolver.resolve(properties.getProperty(MONGODB_DATABASE_USERNAME));
    }
}
