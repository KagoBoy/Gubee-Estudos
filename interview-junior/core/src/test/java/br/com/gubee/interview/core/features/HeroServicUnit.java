package br.com.gubee.interview.core.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.features.hero.HeroRepository;
import br.com.gubee.interview.core.features.hero.HeroService;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

@ExtendWith(MockitoExtension.class)
public class HeroServicUnit {

    @Mock
    private PowerStatsRepository powerStatsRepository;

    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private HeroService heroService;

    @Test
    public void createHeroWithAllRequiredArguments() {

        UUID powerStatsId = UUID.randomUUID();
        UUID heroId = UUID.randomUUID();

        when(powerStatsRepository.create(any(PowerStats.class))).thenReturn(powerStatsId);

        when(heroRepository.create(any(Hero.class))).thenReturn(heroId);

        UUID result = heroService.create(createHeroRequest());

        assertEquals(heroId, result);

    }

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

        when(heroRepository.findById(heroId)).thenReturn(Optional.of(existingHero))
                .thenReturn(Optional.of(updatedHero));
        doNothing().when(powerStatsRepository).update(any(PowerStats.class));
        doNothing().when(heroRepository).updateById(any(Hero.class), any(UUID.class));

        Hero result = heroService.updateById(createHeroRequest(), heroId);

        assertNotNull(result);
        assertEquals(heroId, result.getId());
        assertEquals("Lanterna Verde", result.getName());
        assertEquals(Race.ALIEN, result.getRace());
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

        when(heroRepository.findByName(findName)).thenReturn(Optional.of(existingHero))
                .thenReturn(Optional.of(updatedHero));
        doNothing().when(powerStatsRepository).update(any(PowerStats.class));
        doNothing().when(heroRepository).updateByName(any(Hero.class), eq(findName));

        Hero result = heroService.updateByName(createHeroRequest(), findName);

        assertNotNull(result);
        assertEquals(heroId, result.getId());
        assertEquals("Superman", result.getName());
        assertEquals(Race.DIVINE, result.getRace());
    }

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

}
