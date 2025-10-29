package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.domain.ports.in.CompareService;
import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.domain.services.hero.CompareHeroesService;
import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;

@ExtendWith(MockitoExtension.class)
public class CompareHeroesServiceUnitTest {

    @Mock
    private FindRepository<Optional<Hero>> findHeroRepository;

    @Mock
    private FindRepository<PowerStats> findPowerStatsRepository;

    private CompareService<ComparisonResponse> compareHeroesService;

    @BeforeEach
    public void setUp() {
        compareHeroesService = new CompareHeroesService(findHeroRepository, findPowerStatsRepository);
    }

    @Test
        public void ComparisonResponseTest() {
                UUID hero1Id = UUID.randomUUID();
                UUID powerStats1Id = UUID.randomUUID();
                UUID hero2Id = UUID.randomUUID();
                UUID powerStats2Id = UUID.randomUUID();

                Hero hero1 = Hero.builder()
                                .id(hero1Id)
                                .name("Yan")
                                .race(Race.CYBORG)
                                .powerStatsId(powerStats1Id)
                                .build();

                Hero hero2 = Hero.builder()
                                .id(hero2Id)
                                .name("Superman")
                                .race(Race.DIVINE)
                                .powerStatsId(powerStats2Id)
                                .build();

                PowerStats powerStats1 = PowerStats.builder()
                                .id(powerStats1Id)
                                .strength(10)
                                .agility(8)
                                .dexterity(7)
                                .intelligence(9)
                                .build();

                PowerStats powerStats2 = PowerStats.builder()
                                .id(powerStats2Id)
                                .strength(2)
                                .agility(3)
                                .dexterity(5)
                                .intelligence(4)
                                .build();

                when(findHeroRepository.findById(hero1Id)).thenReturn(Optional.of(hero1));
                when(findHeroRepository.findById(hero2Id)).thenReturn(Optional.of(hero2));
                when(findPowerStatsRepository.findById(powerStats1Id)).thenReturn(powerStats1);
                when(findPowerStatsRepository.findById(powerStats2Id)).thenReturn(powerStats2);

                ComparisonResponse result = compareHeroesService.compare(hero1Id, hero2Id);

                assertNotNull(result);
                assertEquals(8, result.getStrength());
                assertEquals(5, result.getAgility());
                assertEquals(2, result.getDexterity());
                assertEquals(5, result.getIntelligence());
                assertEquals(hero1Id, result.getHero1Id());
                assertEquals(hero2Id, result.getHero2Id());

        }
}
