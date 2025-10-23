package br.com.gubee.interview.core.features.interfaces;

import java.util.UUID;

import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.request.CreateHeroRequest;

public interface IHeroService {
    UUID create(CreateHeroRequest request);
    Hero updateById(CreateHeroRequest request, UUID id);
    Hero updateByName(CreateHeroRequest request, String name);
    void deleteById(UUID id);
    void deleteByName(String name);
    HeroResponse findByName(String name);
    HeroResponse findById(UUID id);
    ComparisonResponse compareHeroes(UUID hero1Id, UUID hero2Id);
}
