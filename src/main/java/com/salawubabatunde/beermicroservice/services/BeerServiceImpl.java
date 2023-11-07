package com.salawubabatunde.beermicroservice.services;

import com.salawubabatunde.beermicroservice.domain.Beer;
import com.salawubabatunde.beermicroservice.repository.BeerRepository;
import com.salawubabatunde.beermicroservice.web.mappers.BeerMapper;
import com.salawubabatunde.beermicroservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    @Override
    public BeerDto getById(UUID beerId) throws NotFoundException {

            return beerMapper.beerToBeerDto(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) throws NotFoundException {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
