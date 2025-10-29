package br.com.gubee.interview.core.domain.services.powerstats;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gubee.interview.core.domain.ports.in.CreateService;
import br.com.gubee.interview.core.domain.ports.out.CreateRepository;
import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePowerStatsService implements CreateService<PowerStats>{

    private final CreateRepository<PowerStats> createPowerStatsRepository;
    @Override
    public UUID create(PowerStats powerStats) {
        return createPowerStatsRepository.create(powerStats);
    }

}
