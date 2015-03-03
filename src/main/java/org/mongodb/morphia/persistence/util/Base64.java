package org.mongodb.morphia.persistence.util;

import java.io.Serializable;

public final class Base64 implements Serializable {

    private static final long serialVersionUID = 6883447314850290695L;

    public static String encode(final String value) {
        return java.util.Base64.getEncoder()
                               .encodeToString(value.getBytes());
    }

    public static String decode(final String value) {
        return new String(java.util.Base64.getDecoder()
                                          .decode(value));
    }
}
