package com.salawubabatunde.beermicroservice.repository;

import com.salawubabatunde.beermicroservice.domain.Beer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {
}
