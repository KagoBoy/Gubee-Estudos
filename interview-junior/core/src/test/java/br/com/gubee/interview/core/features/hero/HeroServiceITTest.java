package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class HeroServiceITTest {

    @Autowired
    private PowerStatsRepository powerStatsRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private HeroService heroService;

    @Transactional
    @Test
    public void createHeroWithAllRequiredArguments() {

        CreateHeroRequest request = createHeroRequest();

        UUID heroId = heroService.create(request);

        Optional<Hero> savedHero = heroRepository.findById(heroId);
        assertTrue(savedHero.isPresent());
        Hero hero = savedHero.get();
        assertEquals(request.getName(), hero.getName());
        assertEquals(request.getRace(), hero.getRace());

        UUID powerStatsId = hero.getPowerStatsId();
        PowerStats savedPowerStats = powerStatsRepository.findById(powerStatsId);
        assertTrue(!savedPowerStats.equals(null));

        assertEquals(request.getStrength(), savedPowerStats.getStrength());
        assertEquals(request.getAgility(), savedPowerStats.getAgility());
        assertEquals(request.getDexterity(), savedPowerStats.getDexterity());
        assertEquals(request.getIntelligence(), savedPowerStats.getIntelligence());
    }

    @Transactional
    @Test
    public void updateHeroById() {

        CreateHeroRequest request = createHeroRequest();
        UUID heroId = heroService.create(request);
        
        CreateHeroRequest updateRequest = updateRequest();
        heroService.updateById(updateRequest, heroId);
        

        Optional<Hero> savedHero = heroRepository.findById(heroId);
        assertTrue(savedHero.isPresent());


        Hero updatedHero = savedHero.get();

        assertEquals(updateRequest.getName(), updatedHero.getName());
        assertEquals(updateRequest.getRace(), updatedHero.getRace());

        UUID powerStatsId = updatedHero.getPowerStatsId();
        PowerStats updatedPowerStats = powerStatsRepository.findById(powerStatsId);
        assertFalse(updatedPowerStats.equals(null));

        assertEquals(updateRequest.getStrength(), updatedPowerStats.getStrength());
        assertEquals(updateRequest.getAgility(), updatedPowerStats.getAgility());
        assertEquals(updateRequest.getDexterity(), updatedPowerStats.getDexterity());
        assertEquals(updateRequest.getIntelligence(), updatedPowerStats.getIntelligence());
    }

    @Transactional
    @Test
    public void delteById(){
        CreateHeroRequest request = createHeroRequest();
        UUID heroId = heroService.create(request);
        Optional<Hero> savedHero = heroRepository.findById(heroId);
        assertTrue(savedHero.isPresent());



        heroService.deleteById(heroId);

        Optional<Hero> deletedHero = heroRepository.findById(heroId);
        
        assertFalse(deletedHero.isPresent());

        assertThrows(ResponseStatusException.class, () -> {
            heroService.findById(heroId);
        });

    }



    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Teste4")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    private CreateHeroRequest updateRequest() {
        return CreateHeroRequest.builder()
                .name("Wonder Woman")
                .agility(8)
                .dexterity(9)
                .strength(7)
                .intelligence(10)
                .race(Race.ALIEN)
                .build();
    }
}
