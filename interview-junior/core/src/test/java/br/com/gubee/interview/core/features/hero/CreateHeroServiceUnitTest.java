package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.domain.ports.out.CreateRepository;
import br.com.gubee.interview.core.domain.services.hero.CreateHeroService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;

@ExtendWith(MockitoExtension.class)
public class CreateHeroServiceUnitTest {

    @Mock
    private CreateRepository<Hero> createHeroRepository;

    @Mock
    private CreateRepository<PowerStats> createPowerStatsRepository;

    private CreateHeroService createHeroService;

    @BeforeEach
    public void setUp() {
        createHeroService = new CreateHeroService(createPowerStatsRepository, createHeroRepository);
    }

    private Requests request = new Requests();

    @Test
    public void createHeroWithAllRequiredArguments() {

        UUID powerStatsId = UUID.randomUUID();
        UUID heroId = UUID.randomUUID();

        when(createPowerStatsRepository.create(any(PowerStats.class))).thenReturn(powerStatsId);

        when(createHeroRepository.create(any(Hero.class))).thenReturn(heroId);
        

        UUID result = createHeroService.create(request.createHeroRequest());

        assertEquals(heroId, result);

    }

}
