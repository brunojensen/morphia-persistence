package org.mongodb.morphia.persistence;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.JAXBContext;
import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.persistence.util.PersistenceUnitInfo;
import com.sun.java.xml.ns.persistence.Persistence;
import com.sun.java.xml.ns.persistence.Persistence.PersistenceUnit.Properties;

final class PersistenceUnit implements Serializable {

    private static final String DEFAULT_PROVIDER = "org.mongodb.morphia.persistence.MorphiaProvider";
    private static final Logger LOGGER = MorphiaLoggerFactory.get(PersistenceUnit.class);
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
        return properties.getProperty("mongodb.database.name");
    }

    String host() {
        return properties.getProperty("mongodb.database.host");
    }

    boolean ignoreInvalid() {
        return Boolean.valueOf(properties.getProperty("mongodb.morphia.ignore_invalid"));
    }

    boolean load() throws Exception {
        final ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
        final List<Persistence.PersistenceUnit> elements = ((Persistence) JAXBContext.newInstance(Persistence.class)
            .createUnmarshaller()
            .unmarshal(classLoader.getResourceAsStream("META-INF"
                + File.separator
                + "persistence.xml"))).getPersistenceUnit();
        loadPersistenceUnit(elements);
        loadProperties();
        return true;
    }

    private void loadPersistenceUnit(final List<Persistence.PersistenceUnit> elements) {
        for (final Persistence.PersistenceUnit element : elements) {
            if (DEFAULT_PROVIDER.equalsIgnoreCase(element.getProvider())
                && unitName.equalsIgnoreCase(element.getName())) {
                persistence = element;
                break;
            }
        }
        if (null == persistence) {
            final String msg = String.format("Persistence Unit with Provider %s not present in persistence.xml",
                                             DEFAULT_PROVIDER);
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
        return properties.getProperty("mongodb.morphia.package");
    }

    int port() {
        return Integer.valueOf(properties.getProperty("mongodb.database.port"));
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
}
