package com.salawubabatunde.beermicroservice.repository;

import com.salawubabatunde.beermicroservice.domain.Beer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BeerRepository extends CrudRepository<Beer, UUID> {
}
