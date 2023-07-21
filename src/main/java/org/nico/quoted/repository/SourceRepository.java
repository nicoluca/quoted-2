package org.nico.quoted.repository;

import org.nico.quoted.domain.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SourceRepository extends PagingAndSortingRepository<Source, Long>, CrudRepository<Source, Long> {

    Page<Source> findAllByUserId(long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM sources
            WHERE sources.id NOT IN (
                SELECT DISTINCT quotes.source_id
                FROM quotes
                WHERE quotes.source_id IS NOT NULL
            );
            """
            , nativeQuery = true)
    void deleteEmptySources();

    Optional<Source> findByName(String name); // TODO Should be by name and user, should be optional
}
