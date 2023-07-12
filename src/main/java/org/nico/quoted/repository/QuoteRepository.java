package org.nico.quoted.repository;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.projections.QuoteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(collectionResourceRel = "quotes", path = "quotes", excerptProjection = QuoteProjection.class)
public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long>, CrudRepository<Quote, Long> {

    Page<Quote> findByTextContainingIgnoreCase(@RequestParam("text") String text, Pageable pageable);

    Page<Quote> findBySourceId(@RequestParam("id") Long id, Pageable pageable);

    Page<Quote> findBySourceIsNull(Pageable pageable);
}
