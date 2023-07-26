package org.nico.quoted.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Logger;

@Configuration
public class RestConfig implements RepositoryRestConfigurer, WebMvcConfigurer {

    private final Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    @Value("${allowed.methods}")
    private String[] allowedMethods;

    private final EntityManager entityManager;

    @Autowired
    public RestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        exposeEntityIds(config);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Configuring CORS and allowed methods...");
        logger.info("Allowed origins: " + String.join(", ", this.allowedOrigins));
        logger.info("Allowed methods: " + String.join(", ", this.allowedMethods));

        registry.addMapping("/api/**")
                .allowedOrigins(this.allowedOrigins)
                .allowedMethods(this.allowedMethods);
    }

    private void exposeEntityIds(RepositoryRestConfiguration config) {
        logger.info("Applying configuration to expose entity ids...");

        var entities = entityManager.getMetamodel().getEntities();

        var entityTypes = entities.stream()
                .map(Type::getJavaType)
                .toArray(Class[]::new);

        config.exposeIdsFor(entityTypes);
    }
}
