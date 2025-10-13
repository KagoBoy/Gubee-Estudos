package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.ComparisonResponse;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.HeroResponse;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, value = "/update/{id}")
    public ResponseEntity<Hero> updateById(@Validated
                                           @RequestBody CreateHeroRequest createHeroRequest, @PathVariable UUID id) {
        Hero hero = heroService.updateById(createHeroRequest, id);
        return ResponseEntity.ok(hero);
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<HeroResponse> findByName(@PathVariable String name) {
        HeroResponse hero = heroService.findByName(name);
        return ResponseEntity.ok(hero);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<HeroResponse> findById(@PathVariable UUID id) {
        HeroResponse hero = heroService.findById(id);
        return ResponseEntity.ok(hero);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        heroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete/{name}")
    public ResponseEntity<Void> deleteById(@PathVariable String name){
        heroService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/compare")
    public ResponseEntity<ComparisonResponse> compareHeroes(@RequestParam UUID hero1Id,
                                                            @RequestParam UUID hero2Id) {
        ComparisonResponse comparison = heroService.compareHeroes(hero1Id, hero2Id);
        return ResponseEntity.ok(comparison);
    }
}
