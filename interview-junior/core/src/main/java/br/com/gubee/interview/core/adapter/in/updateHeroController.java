package br.com.gubee.interview.core.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.core.domain.ports.in.updateService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class updateHeroController {

    private final updateService<CreateHeroRequest, Hero> updateHeroService;

    @PutMapping(consumes = APPLICATION_JSON_VALUE, value = "/update/{id}")
    public ResponseEntity<Hero> updateById(@Validated
                                           @RequestBody CreateHeroRequest createHeroRequest, @PathVariable UUID id) {
        Hero hero = updateHeroService.updateById(createHeroRequest, id);
        return ResponseEntity.ok(hero);                                  
    }
}
