package br.com.gubee.interview.core.adapter.out.powerstats;

import java.util.Map;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.core.domain.ports.out.FindRepository;
import br.com.gubee.interview.core.exception.PowerStatsNotFoundException;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindPowerStatsRepository implements FindRepository<PowerStats> {

    private static final String FIND_POWER_STATS_ID_QUERY = "SELECT * FROM power_stats" +
            " WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
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
                            .build());
            return powerStats;
        } catch (EmptyResultDataAccessException e) {
            throw new PowerStatsNotFoundException("Nulo");
        }
    }

}
