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

import br.com.gubee.interview.core.domain.ports.in.FindService;
import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.domain.services.hero.FindHeroService;
import br.com.gubee.interview.core.domain.services.hero.HeroResponseMapper;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;

@ExtendWith(MockitoExtension.class)
public class FindHeroServiceUnitTest {

        @Mock
        FindRepository<Optional<Hero>> findHeroRepository;

        @Mock
        HeroResponseMapper heroResponseMapper;

        FindService<HeroResponse> findHeroService;

        @BeforeEach
        public void setUp() {
                findHeroService = new FindHeroService(findHeroRepository, heroResponseMapper);
        }

        @Test
        public void findById() {
                UUID heroId = UUID.randomUUID();
                UUID powerStatsId = UUID.randomUUID();
                Hero existingHero = Hero.builder()
                                .id(heroId)
                                .name("Yan")
                                .race(Race.CYBORG)
                                .powerStatsId(powerStatsId)
                                .build();

                PowerStats powerStats = PowerStats.builder()
                                .id(powerStatsId)
                                .strength(10)
                                .agility(8)
                                .dexterity(7)
                                .intelligence(9)
                                .build();

                HeroResponse heroResponse = HeroResponse.builder()
                                .id(existingHero.getId())
                                .name(existingHero.getName())
                                .race(existingHero.getRace())
                                .powerStats(powerStats)
                                .createdAt(existingHero.getCreatedAt())
                                .updatedAt(existingHero.getUpdatedAt())
                                .build();
                when(findHeroRepository.findById(heroId)).thenReturn(Optional.of(existingHero));
                when(heroResponseMapper.toResponse(existingHero)).thenReturn(heroResponse);

                HeroResponse result = findHeroService.findById(heroId);

                assertNotNull(result);
                assertEquals(heroId, result.getId());
                assertEquals("Yan", result.getName());
                assertEquals(8, result.getPowerStats().getAgility());
                assertEquals(Race.CYBORG, result.getRace());

        }

        @Test
        public void findByName() {
                UUID heroId = UUID.randomUUID();
                UUID powerStatsId = UUID.randomUUID();
                final String findName = "Yan";
                Hero existingHero = Hero.builder()
                                .id(heroId)
                                .name("Yan")
                                .race(Race.CYBORG)
                                .powerStatsId(powerStatsId)
                                .build();

                PowerStats powerStats = PowerStats.builder()
                                .id(powerStatsId)
                                .strength(10)
                                .agility(8)
                                .dexterity(7)
                                .intelligence(9)
                                .build();

                HeroResponse heroResponse = HeroResponse.builder()
                                .id(existingHero.getId())
                                .name(existingHero.getName())
                                .race(existingHero.getRace())
                                .powerStats(powerStats)
                                .createdAt(existingHero.getCreatedAt())
                                .updatedAt(existingHero.getUpdatedAt())
                                .build();
                when(findHeroRepository.findByName(findName)).thenReturn(Optional.of(existingHero));
                when(heroResponseMapper.toResponse(existingHero)).thenReturn(heroResponse);

                HeroResponse result = findHeroService.findByName(findName);

                assertNotNull(result);
                assertEquals(heroId, result.getId());
                assertEquals("Yan", result.getName());
                assertEquals(7, result.getPowerStats().getDexterity());
                assertEquals(Race.CYBORG, result.getRace());

        }

}
