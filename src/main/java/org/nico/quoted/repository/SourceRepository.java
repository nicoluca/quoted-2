package org.nico.quoted.repository;

import org.nico.quoted.domain.Source;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "sources", path = "sources")
public interface SourceRepository extends PagingAndSortingRepository<Source, Long> {
}
