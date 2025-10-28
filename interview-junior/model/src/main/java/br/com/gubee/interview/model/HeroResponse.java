package br.com.gubee.interview.model;

import java.time.Instant;
import java.util.UUID;

import br.com.gubee.interview.model.enums.Race;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeroResponse {
    private UUID id;
    private String name;
    private Race race;
    private PowerStats powerStats;
    private Instant createdAt;
    private Instant updatedAt;

    

    public HeroResponse(UUID id, String name, Race race, PowerStats powerStats, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.powerStats = powerStats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Race getRace() {
        return race;
    }
    public PowerStats getPowerStats() {
        return powerStats;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    

}
