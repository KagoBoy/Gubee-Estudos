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
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Race getRace() {
        return race;
    }
    public void setRace(Race race) {
        this.race = race;
    }
    public PowerStats getPowerStats() {
        return powerStats;
    }
    public void setPowerStats(PowerStats powerStats) {
        this.powerStats = powerStats;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    

}
