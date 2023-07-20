package org.nico.quoted.repository;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.projections.QuoteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "quotes", path = "quotes", excerptProjection = QuoteProjection.class)
public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long>, CrudRepository<Quote, Long> {

    Page<Quote> findAllByUserId(@RequestParam("userId") UUID userId, Pageable pageable);

    @Query("SELECT q FROM Quote q " +
            "LEFT JOIN q.source s " +
            "WHERE " +
            "(UPPER(q.text) LIKE CONCAT('%', UPPER(?1), '%') " +
            "OR UPPER(s.name) LIKE CONCAT('%', UPPER(?1), '%'))" +
            "AND q.user.id = ?2")
    Page<Quote> findByText
        (@RequestParam("text") String text, @RequestParam("userId") UUID userId, Pageable pageable);

    Page<Quote> findBySourceIdAndUserId(@RequestParam("id") Long id, @RequestParam("userId") UUID userId, Pageable pageable);

    Page<Quote> findBySourceIsNullAndUserId(@RequestParam("userId") UUID userId, Pageable pageable);
}
