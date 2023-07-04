package org.nico.quoted.repository;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.projections.QuoteProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// TODO allow cross origin
@RepositoryRestResource(collectionResourceRel = "quotes", path = "quotes", excerptProjection = QuoteProjection.class)
public interface QuoteRepository extends CrudRepository<Quote, Long> {
}
