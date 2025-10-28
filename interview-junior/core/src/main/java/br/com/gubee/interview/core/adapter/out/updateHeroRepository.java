package br.com.gubee.interview.core.adapter.out;

import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.core.domain.ports.out.updateRepository;
import br.com.gubee.interview.model.Hero;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class updateHeroRepository implements updateRepository<Hero>{

    private static final String UPDATE_HERO_ID_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, power_stats_id = :powerStatsId" +
            " WHERE id = :id";

    private static final String UPDATE_HERO_NAME_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, powerStatsId = :powerStatsId" +
            " WHERE name = :oldName";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void updateById(Hero hero, UUID id) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId(),
                "id", id);

        namedParameterJdbcTemplate.update(UPDATE_HERO_ID_QUERY, params);
    }

    @Override
    public void updateByName(Hero hero, String name) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId(),
                "oldName", name);

        namedParameterJdbcTemplate.update(UPDATE_HERO_NAME_QUERY, params);
    }
}
