package br.com.gubee.interview.core.domain.services.hero;

import org.springframework.stereotype.Service;

import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeroResponseMapper {
    private final FindRepository<PowerStats> findPowerStatsRepository;

    public HeroResponse toResponse (Hero hero){
        PowerStats powerStats = findPowerStatsRepository.findById(hero.getPowerStatsId());

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
