package guru.springframework.msscbrewery.services;


import guru.springframework.msscbrewery.web.model.BeerDto;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class BeerServiceImpl implements  BeerService{
    private final Logger log = LoggerFactory.getLogger(BeerServiceImpl.class);

    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder().id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle("Pale Ale")
                .build();
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName(beerDto.getBeerName())
                .beerStyle(beerDto.getBeerStyle())
                .build();
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        // todo - impelement real service
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting Beer with ID " + id);
    }
}
