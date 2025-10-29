package br.com.gubee.interview.core.adapter.in.hero;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.core.domain.ports.in.DeleteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class DeleteHeroController {
    private final DeleteService deleteHeroService;

    @DeleteMapping(value = "/delete/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        deleteHeroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete/name/{name}")
    public ResponseEntity<Void> deleteById(@PathVariable String name) {
        deleteHeroService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }
}
