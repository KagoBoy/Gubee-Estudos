package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

        private final HeroRepository heroRepository;
        private final PowerStatsRepository powerStatsRepository;

        @Transactional
        public UUID create(CreateHeroRequest createHeroRequest) {
                PowerStats powerStats = PowerStats.builder()
                                .strength(createHeroRequest.getStrength())
                                .agility(createHeroRequest.getAgility())
                                .dexterity(createHeroRequest.getDexterity())
                                .intelligence(createHeroRequest.getIntelligence())
                                .build();

                UUID powerStatsId = powerStatsRepository.create(powerStats);

                // Cria o Hero com o powerStatsId
                Hero hero = Hero.builder()
                                .name(createHeroRequest.getName())
                                .race(createHeroRequest.getRace())
                                .powerStatsId(powerStatsId)
                                .build();

                return heroRepository.create(hero);
        }

        @Transactional
        public Hero updateById(CreateHeroRequest createHeroRequest, UUID id) {

                Hero existingHero = heroRepository.findById(id)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + id));

                PowerStats powerStats = PowerStats.builder()
                                .id(existingHero.getPowerStatsId())
                                .strength(createHeroRequest.getStrength())
                                .agility(createHeroRequest.getAgility())
                                .dexterity(createHeroRequest.getDexterity())
                                .intelligence(createHeroRequest.getIntelligence())
                                .build();

                powerStatsRepository.update(powerStats);

                Hero hero = Hero.builder()
                                .name(createHeroRequest.getName())
                                .race(createHeroRequest.getRace())
                                .powerStatsId(existingHero.getPowerStatsId())
                                .build();

                heroRepository.updateById(hero, id);

                return heroRepository.findById(id)
                                .orElseThrow(() -> new HeroNotFoundException(
                                                "Hero not found after update with id: " + id));
        }

        @Transactional
        public Hero updateByName(CreateHeroRequest createHeroRequest, String name) {

                Hero existingHero = heroRepository.findByName(name)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with name: " + name));

                PowerStats powerStats = PowerStats.builder()
                                .id(existingHero.getPowerStatsId())
                                .strength(createHeroRequest.getStrength())
                                .agility(createHeroRequest.getAgility())
                                .dexterity(createHeroRequest.getDexterity())
                                .intelligence(createHeroRequest.getIntelligence())
                                .build();

                powerStatsRepository.update(powerStats);

                Hero hero = Hero.builder()
                                .name(createHeroRequest.getName())
                                .race(createHeroRequest.getRace())
                                .powerStatsId(existingHero.getPowerStatsId())
                                .build();

                heroRepository.updateByName(hero, name);

                return heroRepository.findByName(name)
                                .orElseThrow(() -> new HeroNotFoundException(
                                                "Hero not found after update with name: " + name));
        }

        @Transactional
        public void deleteById(UUID id) {
                heroRepository.deleteById(id);
        }

        @Transactional
        public void deleteByName(String name) {
                heroRepository.deleteByName(name);
        }

        public HeroResponse findByName(String name) {
                Optional<Hero> heroOpt = heroRepository.findByName(name);
                if (heroOpt.isPresent()) {
                        Hero hero = heroOpt.get();
                        PowerStats powerStats = powerStatsRepository.findById(hero.getPowerStatsId());
                        HeroResponse response = HeroResponse.builder()
                                        .id(hero.getId())
                                        .name(hero.getName())
                                        .race(hero.getRace())
                                        .powerStats(powerStats)
                                        .createdAt(hero.getCreatedAt())
                                        .updatedAt(hero.getUpdatedAt())
                                        .build();
                        return response;
                } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found");
                }
        }

        public HeroResponse findById(UUID id) {
                Optional<Hero> heroOpt = heroRepository.findById(id);
                if (heroOpt.isPresent()) {
                        Hero hero = heroOpt.get();
                        PowerStats powerStats = powerStatsRepository.findById(hero.getPowerStatsId());
                        HeroResponse response = HeroResponse.builder()
                                        .id(hero.getId())
                                        .name(hero.getName())
                                        .race(hero.getRace())
                                        .powerStats(powerStats)
                                        .createdAt(hero.getCreatedAt())
                                        .updatedAt(hero.getUpdatedAt())
                                        .build();
                        return response;
                } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero not found");
                }
        }

        public ComparisonResponse compareHeroes(UUID hero1Id, UUID hero2Id) {
                Hero hero1 = heroRepository.findById(hero1Id)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + hero1Id));
                Hero hero2 = heroRepository.findById(hero2Id)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + hero2Id));

                PowerStats stats1 = powerStatsRepository.findById(hero1.getPowerStatsId());
                PowerStats stats2 = powerStatsRepository.findById(hero2.getPowerStatsId());

                short strengthDiff = (short) (stats1.getStrength() - stats2.getStrength());
                short agilityDiff = (short) (stats1.getAgility() - stats2.getAgility());
                short dexterityDiff = (short) (stats1.getDexterity() - stats2.getDexterity());
                short intelligenceDiff = (short) (stats1.getIntelligence() - stats2.getIntelligence());

                return ComparisonResponse.builder()
                                .hero1Id(hero1Id)
                                .hero_1(hero1.getName())
                                .hero2Id(hero2Id)
                                .hero_2(hero2.getName())
                                .strength(strengthDiff)
                                .agility(agilityDiff)
                                .dexterity(dexterityDiff)
                                .intelligence(intelligenceDiff)
                                .build();
        }
}
