package br.com.gubee.interview.core.features.factories;

import br.com.gubee.interview.core.features.hero.HeroResponseMapper;
import br.com.gubee.interview.core.features.hero.HeroService;
import br.com.gubee.interview.core.features.interfaces.IHeroRepository;
import br.com.gubee.interview.core.features.interfaces.IHeroService;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeroServiceFactory {

    private final IHeroRepository heroRepository;
    private final PowerStatsRepository powerStatsRepository;
    private final HeroResponseMapper heroResponseMapper;

    public IHeroService create() {
        return new HeroService(heroRepository, powerStatsRepository, heroResponseMapper);
    }

}
