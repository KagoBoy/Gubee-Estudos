package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.com.gubee.interview.model.Hero;
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

}
