package br.com.gubee.interview.core.domain.services.hero;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gubee.interview.core.domain.ports.in.CreateService;
import br.com.gubee.interview.core.domain.ports.out.CreateRepository;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateHeroService implements CreateService<CreateHeroRequest> {

    private final CreateRepository<PowerStats> createPowerStatsRepository;
    private final CreateRepository<Hero> createHeroRepository;

    @Override
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID powerStatsId = createPowerStatsRepository.create(buildPowerStats(createHeroRequest));
        Hero hero = buildHero(createHeroRequest, powerStatsId);

        return createHeroRepository.create(hero);
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
