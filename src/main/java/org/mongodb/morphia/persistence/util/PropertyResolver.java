package org.mongodb.morphia.persistence.util;

import java.io.Serializable;

public final class PropertyResolver implements Serializable {

    private static final String EMPTY = "";

    private static final long serialVersionUID = 4079282732326730115L;

    public static final String environment(final String value) {
        return System.getenv()
            .get(value);
    }

    private static final boolean isBlank(final String value) {
        return null == value || value.isEmpty();
    }

    private static final boolean isVariable(final String value) {
        return value.startsWith("$");
    }

    public static final String resolve(final String value) {
        if (!isBlank(value) && isVariable(value)) {
            final String variable = value.substring(1);
            String returnValue = environment(variable);
            if (isBlank(returnValue)) {
                returnValue = systemProperties(variable);
            }
            return returnValue;
        }
        return value;
    }

    public static final String systemProperties(final String value) {
        return System.getProperty(value, EMPTY);
    }
}
