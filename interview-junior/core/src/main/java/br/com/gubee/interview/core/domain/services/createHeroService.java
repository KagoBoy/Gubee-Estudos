package br.com.gubee.interview.core.domain.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gubee.interview.core.domain.ports.in.createService;
import br.com.gubee.interview.core.domain.ports.out.createRepository;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class createHeroService implements createService<CreateHeroRequest> {

    private final PowerStatsRepository powerStatsRepository;
    private final createRepository<Hero> createHeroRepository;

    @Override
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID powerStatsId = powerStatsRepository.create(buildPowerStats(createHeroRequest));
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
