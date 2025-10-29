package br.com.gubee.interview.core.adapter.out.powerstats;

import java.util.UUID;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.core.domain.ports.out.UpdateRepository;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UpdatePowerStatsRepository implements UpdateRepository<PowerStats>{

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats" +
            " SET strength = :strength, agility = :agility, dexterity = :dexterity, intelligence = :intelligence" +
            " WHERE id = :id";
    
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public void updateById(PowerStats powerStats, UUID id) {
        namedParameterJdbcTemplate.update(
                UPDATE_POWER_STATS_QUERY,
                new BeanPropertySqlParameterSource(powerStats));
    }

}
