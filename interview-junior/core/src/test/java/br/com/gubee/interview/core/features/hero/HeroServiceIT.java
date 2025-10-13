package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class HeroServiceIT {

    @Mock
    private PowerStatsRepository powerStatsRepository;

    @Mock
    private HeroRepository heroRepository;

    @Autowired
    private HeroService heroService;

    @Test
    public void createHeroWithAllRequiredArguments() {

        UUID powerStatsId = UUID.randomUUID();
        UUID heroId = UUID.randomUUID();

        PowerStats powerStats = PowerStats.builder()
                .strength(createHeroRequest().getStrength())
                .agility(createHeroRequest().getAgility())
                .dexterity(createHeroRequest().getDexterity())
                .intelligence(createHeroRequest().getIntelligence())
                .build();

        Hero hero = Hero.builder()
            .name(createHeroRequest().getName())
            .race(createHeroRequest().getRace())
            .powerStatsId(powerStatsId)
            .build();
        

        when(powerStatsRepository.create(any(PowerStats.class))).thenReturn(powerStatsId);

        when(heroRepository.create(any(Hero.class))).thenReturn(heroId);

        UUID result = heroService.create(createHeroRequest());

        assertEquals(heroId, result);
    }

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
            .name("Yan")
            .agility(5)
            .dexterity(8)
            .strength(6)
            .intelligence(10)
            .race(Race.HUMAN)
            .build();
    }
}
