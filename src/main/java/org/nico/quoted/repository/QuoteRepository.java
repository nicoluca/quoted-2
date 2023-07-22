package org.nico.quoted.repository;

import org.nico.quoted.domain.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long>, CrudRepository<Quote, Long> {

    Page<Quote> findAllByUserId(long userId, Pageable pageable);

    @Query("SELECT q FROM Quote q " +
            "LEFT JOIN q.source s " +
            "WHERE " +
            "(UPPER(q.text) LIKE CONCAT('%', UPPER(?1), '%') " +
            "OR UPPER(s.name) LIKE CONCAT('%', UPPER(?1), '%'))" +
            "AND q.user.id = ?2")
    Page<Quote> findByText
        (String text, long userId, Pageable pageable);

    Page<Quote> findBySourceIdAndUserId(long id, long userId, Pageable pageable);

    List<Quote> findBySourceIdAndUserId(long id, long userId);

    Page<Quote> findBySourceIsNullAndUserId(long userId, Pageable pageable);

    List<Quote> findBySourceIsNullAndUserId(long userId);

}
