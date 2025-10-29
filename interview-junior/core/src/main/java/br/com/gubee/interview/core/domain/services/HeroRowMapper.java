package br.com.gubee.interview.core.domain.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.enums.Race;

@Component
public class HeroRowMapper implements RowMapper<Hero> {

    @Override
    public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Hero.builder()
                .id(rs.getObject("id", UUID.class))
                .name(rs.getString("name"))
                .race(Race.valueOf(rs.getString("race")))
                .powerStatsId(rs.getObject("power_stats_id", UUID.class))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .build();
    }

}
