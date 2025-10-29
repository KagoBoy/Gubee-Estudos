package br.com.gubee.interview.core.adapter.out;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.core.domain.ports.out.findRepository;
import br.com.gubee.interview.core.domain.services.HeroRowMapper;
import br.com.gubee.interview.model.Hero;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class findHeroRepository implements findRepository<Optional<Hero>>{

    private final HeroRowMapper heroRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_HERO_NAME_QUERY = "SELECT * FROM hero" +
            " WHERE name = :name";

    private static final String FIND_HERO_ID_QUERY = "SELECT * FROM hero" +
            " WHERE id = :id";


    @Override
    public Optional<Hero> findById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);

        try {
            Hero hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_ID_QUERY,
                    params,
                    heroRowMapper);
            return Optional.ofNullable(hero);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Hero> findByName(String name) {
        final Map<String, Object> params = Map.of("name", name);

        try {
            Hero hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_NAME_QUERY,
                    params,
                    heroRowMapper);
            return Optional.ofNullable(hero);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
