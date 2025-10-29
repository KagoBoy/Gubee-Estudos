package br.com.gubee.interview.core.domain.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.gubee.interview.core.domain.ports.in.findService;
import br.com.gubee.interview.core.domain.ports.out.findRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class findHeroService implements findService<HeroResponse> {

    private final findRepository<Optional<Hero>> findHeroRepository;
    private final HeroResponseMapper heroResponseMapper;

    @Override
    public HeroResponse findByName(String name) {
        Hero hero = findHeroRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found"));
        return heroResponseMapper.toResponse(hero);
    }

    @Override
    public HeroResponse findById(UUID id) {
        Hero hero = findHeroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found"));
        return heroResponseMapper.toResponse(hero);
    }

}
