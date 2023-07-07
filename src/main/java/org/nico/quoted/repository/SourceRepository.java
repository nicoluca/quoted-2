package org.nico.quoted.repository;

import org.nico.quoted.domain.Source;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "sources", path = "sources")
public interface SourceRepository extends PagingAndSortingRepository<Source, Long> {
}
