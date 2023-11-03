package com.salawubabatunde.beermicroservice.web.mappers;

import com.salawubabatunde.beermicroservice.domain.Beer;
import com.salawubabatunde.beermicroservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);
}
