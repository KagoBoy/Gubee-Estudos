package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.exception.PowerStatsNotFoundException;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats" +
            " SET strength = :strength, agility = :agility, dexterity = :dexterity, intelligence = :intelligence" +
            " WHERE id = :id";

    private static final String FIND_POWER_STATS_ID_QUERY = "SELECT * FROM power_stats" +
            " WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_POWER_STATS_QUERY,
                new BeanPropertySqlParameterSource(powerStats),
                UUID.class);
    }

    public void update(PowerStats powerStats) {
        namedParameterJdbcTemplate.update(
                UPDATE_POWER_STATS_QUERY,
                new BeanPropertySqlParameterSource(powerStats));
    }

    public PowerStats findById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);

        try {
            PowerStats powerStats = namedParameterJdbcTemplate.queryForObject(
                    FIND_POWER_STATS_ID_QUERY,
                    params,
                    (rs, rowNum) -> PowerStats.builder()
                            .id(rs.getObject("id", UUID.class))
                            .strength(rs.getShort("strength"))
                            .agility(rs.getShort("agility"))
                            .dexterity(rs.getShort("dexterity"))
                            .intelligence(rs.getShort("intelligence"))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .updatedAt(rs.getTimestamp("updated_at").toInstant())
                            .build()
            );
            return powerStats;
        } catch (EmptyResultDataAccessException e) {
            throw new PowerStatsNotFoundException("Nulo");
        }
    }
}
