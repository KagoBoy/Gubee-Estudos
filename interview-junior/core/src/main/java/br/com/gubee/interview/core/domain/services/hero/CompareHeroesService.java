package br.com.gubee.interview.core.domain.services.hero;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gubee.interview.core.domain.ports.in.CompareService;
import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompareHeroesService implements CompareService<ComparisonResponse> {

    private final FindRepository<Optional<Hero>> findHeroRepository;
    private final FindRepository<PowerStats> findPowerStatsRepository;

    @Override
    public ComparisonResponse compare(UUID hero1Id, UUID hero2Id) {
        Hero hero1 = findHeroRepository.findById(hero1Id)
                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + hero1Id));
        Hero hero2 = findHeroRepository.findById(hero2Id)
                .orElseThrow(() -> new HeroNotFoundException("Hero not found with id: " + hero2Id));

        PowerStats stats1 = findPowerStatsRepository.findById(hero1.getPowerStatsId());
        PowerStats stats2 = findPowerStatsRepository.findById(hero2.getPowerStatsId());

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
