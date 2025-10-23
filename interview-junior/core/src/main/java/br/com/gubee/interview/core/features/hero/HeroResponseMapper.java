package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeroResponseMapper {
    private final PowerStatsRepository powerStatsRepository;

    public HeroResponse toResponse (Hero hero){
        PowerStats powerStats = powerStatsRepository.findById(hero.getPowerStatsId());

        return HeroResponse.builder()
            .id(hero.getId())
            .name(hero.getName())
            .race(hero.getRace())
            .powerStats(powerStats)
            .createdAt(hero.getCreatedAt())
            .updatedAt(hero.getUpdatedAt())
            .build();
    }
}
