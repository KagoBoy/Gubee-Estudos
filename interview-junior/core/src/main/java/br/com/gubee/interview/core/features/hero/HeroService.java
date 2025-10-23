package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.core.features.interfaces.IHeroRepository;
import br.com.gubee.interview.core.features.interfaces.IHeroService;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService implements IHeroService {

        private final IHeroRepository heroRepository;
        private final PowerStatsRepository powerStatsRepository;
        private final HeroResponseMapper heroResponseMapper;

        @Transactional
        @Override
        public UUID create(CreateHeroRequest createHeroRequest) {
                UUID powerStatsId = powerStatsRepository.create(buildPowerStats(createHeroRequest));
                Hero hero = buildHero(createHeroRequest, powerStatsId);

                return heroRepository.create(hero);
        }

        @Transactional
        @Override
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

                PowerStats powerStats = PowerStats.builder()
                                .id(existingHero.getPowerStatsId())
                                .strength(createHeroRequest.getStrength())
                                .agility(createHeroRequest.getAgility())
                                .dexterity(createHeroRequest.getDexterity())
                                .intelligence(createHeroRequest.getIntelligence())
                                .build();

                powerStatsRepository.update(powerStats);

                Hero hero = buildHero(createHeroRequest, existingHero.getPowerStatsId());

                heroRepository.updateByName(hero, name);

                return heroRepository.findByName(name)
                                .orElseThrow(() -> new HeroNotFoundException(
                                                "Hero not found after update with name: " + name));
        }

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

        @Override
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

        private PowerStats buildPowerStats(CreateHeroRequest request) {
                return PowerStats.builder()
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
