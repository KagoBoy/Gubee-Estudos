package br.com.gubee.interview.core.domain.services;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.domain.ports.in.updateService;
import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.core.features.hero.HeroRepository;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;

public class updateHeroService implements updateService<CreateHeroRequest, Hero> {

    HeroRepository heroRepository;
    PowerStatsRepository powerStatsRepository;

    @Transactional
    @Override
    public Hero updateById(CreateHeroRequest createHeroRequest, UUID id) {

        Hero existingHero = heroRepository.findById(id)
                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + id));

        PowerStats powerStats = buildPowerStats(createHeroRequest, existingHero);
        powerStatsRepository.update(powerStats);

        Hero hero = buildHero(createHeroRequest, existingHero.getPowerStatsId());

        heroRepository.updateById(hero, id);

        return heroRepository.findById(id)
                .orElseThrow(() -> new HeroNotFoundException(
                        "Hero not found after update with id: " + id));
    }

    @Transactional
    @Override
    public Hero updateByName(CreateHeroRequest createHeroRequest, String name) {

        Hero existingHero = heroRepository.findByName(name)
                .orElseThrow(() -> new HeroNotFoundException("Hero not found with name: " + name));
  
        PowerStats powerStats = buildPowerStats(createHeroRequest, existingHero);

        powerStatsRepository.update(powerStats);

        Hero hero = buildHero(createHeroRequest, existingHero.getPowerStatsId());

        heroRepository.updateByName(hero, name);

        return heroRepository.findByName(name)
                .orElseThrow(() -> new HeroNotFoundException(
                        "Hero not found after update with name: " + name));
    }

    private PowerStats buildPowerStats(CreateHeroRequest request, Hero existingHero) {
        return PowerStats.builder()
                .id(existingHero.getId())
                .strength(request.getStrength())
                .agility(request.getAgility())
                .dexterity(request.getDexterity())
                .intelligence(request.getIntelligence())
                .build();
    }

    private Hero buildHero(CreateHeroRequest request, UUID powerStatsId) {
        return Hero.builder()
                .name(request.getName())
                .race(request.getRace())
                .powerStatsId(powerStatsId)
                .build();
    }
}
