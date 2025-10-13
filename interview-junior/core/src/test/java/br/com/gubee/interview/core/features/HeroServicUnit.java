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
import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
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

        @Test
        public void deleteByName() {
                String heroName = "Yan";

                doNothing().when(heroRepository).deleteByName(heroName);
                heroService.deleteByName(heroName);

                verify(heroRepository, times(1)).deleteByName(heroName);
        }

        @Test
        public void deleteById() {
                UUID heroId = UUID.randomUUID();

                doNothing().when(heroRepository).deleteById(heroId);
                heroService.deleteById(heroId);

                verify(heroRepository, times(1)).deleteById(heroId);
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
                when(heroRepository.findById(heroId)).thenReturn(Optional.of(existingHero));
                when(powerStatsRepository.findById(powerStatsId)).thenReturn(powerStats);

                HeroResponse result = heroService.findById(heroId);

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
                when(heroRepository.findByName(findName)).thenReturn(Optional.of(existingHero));
                when(powerStatsRepository.findById(powerStatsId)).thenReturn(powerStats);

                HeroResponse result = heroService.findByName(findName);

                assertNotNull(result);
                assertEquals(heroId, result.getId());
                assertEquals("Yan", result.getName());
                assertEquals(7, result.getPowerStats().getDexterity());
                assertEquals(Race.CYBORG, result.getRace());

        }

        @Test
        public void ComparisonResponseTest(){
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

                when(heroRepository.findById(hero1Id)).thenReturn(Optional.of(hero1));
                when(heroRepository.findById(hero2Id)).thenReturn(Optional.of(hero2));
                when(powerStatsRepository.findById(powerStats1Id)).thenReturn(powerStats1);
                when(powerStatsRepository.findById(powerStats2Id)).thenReturn(powerStats2);

                ComparisonResponse result = heroService.compareHeroes(hero1Id, hero2Id);


                assertNotNull(result);
                assertEquals(8, result.getStrength());
                assertEquals(5, result.getAgility());
                assertEquals(2, result.getDexterity());
                assertEquals(5, result.getIntelligence());
                assertEquals(hero1Id, result.getHero1Id());
                assertEquals(hero2Id, result.getHero2Id());

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
