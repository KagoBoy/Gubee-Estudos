package br.com.gubee.interview.core.domain.services;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.domain.ports.in.deleteService;
import br.com.gubee.interview.core.features.hero.HeroRepository;

public class deleteHeroService implements deleteService {

    HeroRepository heroRepository;

    @Transactional
    @Override
    public void deleteById(UUID id) {
        heroRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByName(String name) {
        heroRepository.deleteByName(name);
    }
}
