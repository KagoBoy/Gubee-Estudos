package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HeroE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private PowerStatsRepository powerStatsRepository;

    @Test
    void shouldCreateHero_WhenValidRequest() {
        String randomName = UUID.randomUUID().toString().replace("-", "");
        CreateHeroRequest request = CreateHeroRequest.builder()
                .name(randomName)
                .race(Race.HUMAN)
                .strength(6)
                .agility(5)
                .dexterity(8)
                .intelligence(10)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/v1/heroes",
                request,
                Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getHeaders().getLocation());

        String location = response.getHeaders().getLocation().toString();
        String heroId = location.substring(location.lastIndexOf("/") + 1);

        Optional<Hero> savedHero = heroRepository.findById(UUID.fromString(heroId));
        assertTrue(savedHero.isPresent());
        assertEquals(savedHero.get().getName(), randomName);
    }

    @Test
    void shouldUpdateHero_WhenValidRequest() {
        String randomName = UUID.randomUUID().toString().replace("-", "");
        CreateHeroRequest request = CreateHeroRequest.builder()
                .name(randomName)
                .race(Race.HUMAN)
                .strength(6)
                .agility(5)
                .dexterity(8)
                .intelligence(10)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/v1/heroes",
                request,
                Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getHeaders().getLocation());

        String location = response.getHeaders().getLocation().toString();
        String heroId = location.substring(location.lastIndexOf("/") + 1);
        UUID heroUUID = UUID.fromString(heroId);

        Optional<Hero> savedHero = heroRepository.findById(heroUUID);
        assertTrue(savedHero.isPresent());
        assertEquals(savedHero.get().getName(), randomName);
        assertEquals(savedHero.get().getRace(), Race.HUMAN);

        String updatedName = randomName + "_Updated";
        CreateHeroRequest updateRequest = CreateHeroRequest.builder()
                .name(updatedName)
                .race(Race.DIVINE)
                .strength(10)
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateHeroRequest> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<Hero> updateResponse = restTemplate.exchange(
                "/api/v1/heroes/update/" + heroId,
                HttpMethod.PUT,
                updateEntity,
                Hero.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());

        Hero updatedHero = updateResponse.getBody();
        assertEquals(updatedName, updatedHero.getName());
        assertEquals(Race.DIVINE, updatedHero.getRace());

        Optional<Hero> heroFromDb = heroRepository.findById(heroUUID);
        assertTrue(heroFromDb.isPresent());
        assertEquals(updatedName, heroFromDb.get().getName());
        assertEquals(Race.DIVINE, heroFromDb.get().getRace());

        UUID powerStatsId = heroFromDb.get().getPowerStatsId();
        PowerStats updatedPowerStats = powerStatsRepository.findById(powerStatsId);
        assertFalse(updatedPowerStats.equals(null));
        assertEquals((short) 10, updatedPowerStats.getStrength());
        assertEquals((short) 10, updatedPowerStats.getAgility());
        assertEquals((short) 10, updatedPowerStats.getDexterity());
        assertEquals((short) 10, updatedPowerStats.getIntelligence());

    }

}
