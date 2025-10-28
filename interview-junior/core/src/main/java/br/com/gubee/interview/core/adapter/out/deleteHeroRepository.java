package br.com.gubee.interview.core.adapter.out;

import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.core.domain.ports.out.deleteRepository;
import br.com.gubee.interview.core.exception.HeroNotFoundException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class deleteHeroRepository implements deleteRepository{

    private static final String DELETE_HERO_NAME_QUERY = "DELETE FROM hero" +
            " WHERE name = :name";

    private static final String DELETE_HERO_ID_QUERY = "DELETE FROM hero" +
            " WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
            
    @Override
    public void deleteById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        int rowsAffected = namedParameterJdbcTemplate.update(DELETE_HERO_ID_QUERY, params);

        if (rowsAffected == 0) {
            throw new HeroNotFoundException("Hero not found with id: " + id);
        }
    }

    @Override
    public void deleteByName(String name) {
        final Map<String, Object> params = Map.of("name", name);
        int rowsAffected = namedParameterJdbcTemplate.update(DELETE_HERO_NAME_QUERY, params);

        if (rowsAffected == 0) {
            throw new HeroNotFoundException("Hero not found with name: " + name);
        }
    }

}
