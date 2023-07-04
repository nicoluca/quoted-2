package org.nico.quoted.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Autowired
    public RestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        exposeIds(config);

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        var entities = entityManager.getMetamodel().getEntities();

        var entityTypes = entities.stream()
                .map(Type::getJavaType)
                .toArray(Class[]::new);

        config.exposeIdsFor(entityTypes);
    }
}
