package com.salawubabatunde.beermicroservice.services;

import com.salawubabatunde.beermicroservice.web.model.BeerDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.UUID;

public interface BeerService {
    BeerDto getById(UUID beerId) throws ChangeSetPersister.NotFoundException;

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto) throws ChangeSetPersister.NotFoundException;
}
