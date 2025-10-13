package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.HeroNotFoundException;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.enums.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";


    private static final String FIND_HERO_NAME_QUERY = "SELECT * FROM hero" +
            " WHERE name = :name";

    private static final String FIND_HERO_ID_QUERY = "SELECT * FROM hero" +
            " WHERE id = :id";

    private static final String UPDATE_HERO_ID_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, power_stats_id = :powerStatsId" +
            " WHERE id = :id";

    private static final String UPDATE_HERO_NAME_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, powerStatsId = :powerStatsId" +
            " WHERE name = :oldName";

    private static final String DELETE_HERO_NAME_QUERY = "DELETE FROM hero" +
            " WHERE name = :name";

    private static final String DELETE_HERO_ID_QUERY = "DELETE FROM hero" +
            " WHERE id = :id";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }

    void updateById(Hero hero, UUID id) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId(),
                "id", id);


        namedParameterJdbcTemplate.update(UPDATE_HERO_ID_QUERY, params);
    }

    void updateByName(Hero hero, String name) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId(),
                "oldName", name);

        namedParameterJdbcTemplate.update(UPDATE_HERO_NAME_QUERY, params);
    }

    void deleteById(UUID id){
        final Map<String, Object> params = Map.of("id", id);
        int rowsAffected = namedParameterJdbcTemplate.update(DELETE_HERO_ID_QUERY, params);

        if (rowsAffected == 0){
            throw new HeroNotFoundException("Hero not found with id: " + id);
        }
    }

    void deleteByName(String name){
        final Map<String, Object> params = Map.of("name", name);
        int rowsAffected = namedParameterJdbcTemplate.update(DELETE_HERO_NAME_QUERY, params);

        if (rowsAffected == 0){
            throw new HeroNotFoundException("Hero not found with name: " + name);
        }
    }

    Optional<Hero> findByName(String name){
        final Map<String, Object> params = Map.of("name", name);

        try {
            Hero hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_NAME_QUERY,
                    params,
                    (rs, rowNum) -> Hero.builder()
                            .id(rs.getObject("id", UUID.class))
                            .name(rs.getString("name"))
                            .race(Race.valueOf(rs.getString("race")))
                            .powerStatsId(rs.getObject("power_stats_id", UUID.class))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .updatedAt(rs.getTimestamp("updated_at").toInstant())
                            .build()
            );
            return Optional.ofNullable(hero);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    Optional<Hero> findById(UUID id){
        final Map<String, Object> params = Map.of("id", id);


        try {
            Hero hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_ID_QUERY,
                    params,
                    (rs, rowNum) -> Hero.builder()
                            .id(rs.getObject("id", UUID.class))
                            .name(rs.getString("name"))
                            .race(Race.valueOf(rs.getString("race")))
                            .powerStatsId(rs.getObject("power_stats_id", UUID.class))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .updatedAt(rs.getTimestamp("updated_at").toInstant())
                            .build()
            );
            return Optional.ofNullable(hero);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
