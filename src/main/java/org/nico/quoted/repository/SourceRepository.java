package org.nico.quoted.repository;

import org.nico.quoted.domain.Source;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RepositoryRestResource(collectionResourceRel = "sources", path = "sources")
public interface SourceRepository extends PagingAndSortingRepository<Source, Long>, CrudRepository<Source, Long> {
    @RestResource(exported = false)
    Source findByNameAndUserId(String name, UUID userId);

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
}
