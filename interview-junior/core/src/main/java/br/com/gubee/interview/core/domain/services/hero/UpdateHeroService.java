package br.com.gubee.interview.core.domain.services.hero;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.domain.ports.in.UpdateService;
import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.domain.ports.out.UpdateRepository;
import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateHeroService implements UpdateService<CreateHeroRequest, Hero> {

        private final UpdateRepository<Hero> updateHeroRepository;
        private final FindRepository<Optional<Hero>> findHeroRepository;
        private final UpdateRepository<PowerStats> updatePowerStatsRepository;

        @Transactional
        @Override
        public Hero updateById(CreateHeroRequest createHeroRequest, UUID id) {

                Hero existingHero = findHeroRepository.findById(id)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + id));

                PowerStats powerStats = buildPowerStats(createHeroRequest, existingHero);
                updatePowerStatsRepository.updateById(powerStats, existingHero.getPowerStatsId());

                Hero hero = buildHero(createHeroRequest, existingHero.getPowerStatsId());

                updateHeroRepository.updateById(hero, id);

                return findHeroRepository.findById(id)
                                .orElseThrow(() -> new HeroNotFoundException(
                                                "Hero not found after update with id: " + id));
        }

        @Transactional
        @Override
        public Hero updateByName(CreateHeroRequest createHeroRequest, String name) {

                Hero existingHero = findHeroRepository.findByName(name)
                                .orElseThrow(() -> new HeroNotFoundException("Hero not found with name: " + name));

                PowerStats powerStats = buildPowerStats(createHeroRequest, existingHero);

                updatePowerStatsRepository.updateById(powerStats, existingHero.getPowerStatsId());

                Hero hero = buildHero(createHeroRequest, existingHero.getPowerStatsId());

                updateHeroRepository.updateByName(hero, name);

                return findHeroRepository.findByName(name)
                                .orElseThrow(() -> new HeroNotFoundException(
                                                "Hero not found after update with name: " + name));
        }

        private PowerStats buildPowerStats(CreateHeroRequest request, Hero existingHero) {
                return PowerStats.builder()
                                .id(existingHero.getPowerStatsId())
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
