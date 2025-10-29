// package br.com.gubee.interview.core.features.powerstats;

// import br.com.gubee.interview.core.domain.ports.out.CreateRepository;
// import br.com.gubee.interview.model.PowerStats;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class PowerStatsService {

//     private final CreateRepository<PowerStats> createPowerStatsRepository;

//     @Transactional
//     public UUID create(PowerStats powerStats) {
//         return createPowerStatsRepository.create(powerStats);
//     }
// }
