package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CatalogMySqlIntegrationTests {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("catalog_service")
            .withUsername("catalog_user")
            .withPassword("container-only-password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @jakarta.annotation.Resource
    private MockMvc mockMvc;

    @Test
    void flywaySchemaLoadsAndCatalogRequiresReadScope() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/products").with(jwt()
                        .authorities(new SimpleGrantedAuthority("SCOPE_catalog.read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void writesAndReadsProductsFromTheRelationalCatalog() throws Exception {
        mockMvc.perform(post("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_catalog.write")))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Architecture Patterns",
                                  "description": "Database-backed catalog proof",
                                  "priceMinor": 4250,
                                  "currency": "INR",
                                  "categoryName": "books"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());

        mockMvc.perform(get("/products/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_catalog.read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Architecture Patterns"))
                .andExpect(jsonPath("$.priceMinor").value(4250))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.version").isNumber())
                .andExpect(jsonPath("$.price").value(42.5));
    }
}
