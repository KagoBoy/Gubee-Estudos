package br.com.gubee.interview.core.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.core.domain.ports.in.findService;
import br.com.gubee.interview.model.HeroResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class findHeroController {
    private final findService<HeroResponse> findHeroService;

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<HeroResponse> findByName(@PathVariable String name) {
        HeroResponse hero = findHeroService.findByName(name);
        return ResponseEntity.ok(hero);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<HeroResponse> findById(@PathVariable UUID id) {
        HeroResponse hero = findHeroService.findById(id);
        return ResponseEntity.ok(hero);
    }
}
