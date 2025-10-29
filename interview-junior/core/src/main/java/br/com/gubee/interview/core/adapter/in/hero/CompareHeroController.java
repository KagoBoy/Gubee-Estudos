package br.com.gubee.interview.core.adapter.in.hero;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.core.domain.ports.in.CompareService;
import br.com.gubee.interview.model.ComparisonResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class CompareHeroController {

    private final CompareService<ComparisonResponse> compareHeroService;

    @GetMapping(value = "/compare")
    public ResponseEntity<ComparisonResponse> compareHeroes(@RequestParam UUID hero1Id,
            @RequestParam UUID hero2Id) {
        ComparisonResponse comparison = compareHeroService.compare(hero1Id, hero2Id);
        return ResponseEntity.ok(comparison);
    }
}
