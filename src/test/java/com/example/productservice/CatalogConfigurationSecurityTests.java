package com.example.productservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogConfigurationSecurityTests {

    @Test
    void databasePasswordHasNoCommittedFallback() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new IOException("application.properties is missing from the test classpath");
            }
            properties.load(input);
        }

        assertEquals("\u0024{CATALOG_DB_PASSWORD}", properties.getProperty("spring.datasource.password"));
    }
}
