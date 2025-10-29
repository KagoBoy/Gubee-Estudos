package br.com.gubee.interview.core.domain.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.gubee.interview.core.domain.ports.in.findService;
import br.com.gubee.interview.core.features.hero.HeroRepository;
import br.com.gubee.interview.core.features.hero.HeroResponseMapper;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;

public class findHeroService implements findService<HeroResponse> {

    HeroRepository heroRepository;
    HeroResponseMapper heroResponseMapper;
    @Override
    public HeroResponse findByName(String name) {
        Hero hero = heroRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found"));
        return heroResponseMapper.toResponse(hero);
    }

    @Override
    public HeroResponse findById(UUID id) {
        Hero hero = heroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found"));
        return heroResponseMapper.toResponse(hero);
    }

}
