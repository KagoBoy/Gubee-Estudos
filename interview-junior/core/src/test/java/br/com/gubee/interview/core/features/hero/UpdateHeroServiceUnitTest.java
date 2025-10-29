package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.domain.ports.in.UpdateService;
import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.domain.ports.out.UpdateRepository;
import br.com.gubee.interview.core.domain.services.hero.UpdateHeroService;
import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

@ExtendWith(MockitoExtension.class)
public class UpdateHeroServiceUnitTest {

    @Mock
    private FindRepository<Optional<Hero>> findHeroRepository;

    @Mock
    private UpdateRepository<Hero> updateHeroRepository;

    @Mock
    private UpdateRepository<PowerStats> updatePowerStatsRepository;

    private UpdateService<CreateHeroRequest, Hero> updateHeroService;

    @BeforeEach
    public void setUp() {
        updateHeroService = new UpdateHeroService(updateHeroRepository, findHeroRepository, updatePowerStatsRepository);
    }

    private Requests request = new Requests();

    @Test
        public void updateByIdWithAllRequiredArguments() {

                UUID powerStatsId = UUID.randomUUID();
                UUID heroId = UUID.randomUUID();

                Hero existingHero = Hero.builder()
                                .id(heroId)
                                .name("Yan")
                                .race(Race.ALIEN)
                                .powerStatsId(powerStatsId)
                                .build();

                Hero updatedHero = Hero.builder()
                                .id(heroId)
                                .name("Lanterna Verde")
                                .race(Race.ALIEN)
                                .powerStatsId(powerStatsId)
                                .build();

                when(findHeroRepository.findById(heroId)).thenReturn(Optional.of(existingHero))
                                .thenReturn(Optional.of(updatedHero));
                doNothing().when(updatePowerStatsRepository).updateById(any(PowerStats.class), eq(powerStatsId));
                doNothing().when(updateHeroRepository).updateById(any(Hero.class), any(UUID.class));

                Hero result = updateHeroService.updateById(request.createHeroRequest(), heroId);

                assertNotNull(result);
                assertEquals(heroId, result.getId());
                assertEquals("Lanterna Verde", result.getName());
                assertEquals(Race.ALIEN, result.getRace());
        }


        @Test
        public void updateByIdWithIdIncorrect() {
                UUID heroId = UUID.randomUUID();

                when(findHeroRepository.findById(heroId)).thenReturn(Optional.empty());

                HeroNotFoundException exception = assertThrows(HeroNotFoundException.class, 
                () -> updateHeroService.updateById(request.createHeroRequest(), heroId));

                String expected = "Hero not found with id: " + heroId;

                assertEquals(expected, exception.getMessage());
        }

        @Test
        public void updatebyNameWithAllRequiredArguments() {

                UUID heroId = UUID.randomUUID();
                UUID powerStatsId = UUID.randomUUID();
                final String findName = "Yan";

                Hero existingHero = Hero.builder()
                                .id(heroId)
                                .name("Yan")
                                .race(Race.ALIEN)
                                .powerStatsId(powerStatsId)
                                .build();

                Hero updatedHero = Hero.builder()
                                .id(heroId)
                                .name("Superman")
                                .race(Race.DIVINE)
                                .powerStatsId(powerStatsId)
                                .build();

                when(findHeroRepository.findByName(findName)).thenReturn(Optional.of(existingHero))
                                .thenReturn(Optional.of(updatedHero));
                doNothing().when(updatePowerStatsRepository).updateById(any(PowerStats.class), eq(existingHero.getPowerStatsId()));
                doNothing().when(updateHeroRepository).updateByName(any(Hero.class), eq(findName));

                Hero result = updateHeroService.updateByName(request.createHeroRequest(), findName);

                assertNotNull(result);
                assertEquals(heroId, result.getId());
                assertEquals("Superman", result.getName());
                assertEquals(Race.DIVINE, result.getRace());
        }
}
