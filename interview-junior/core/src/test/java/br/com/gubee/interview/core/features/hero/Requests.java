package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

public class Requests {

    public CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    public CreateHeroRequest createHeroRequestWithoutPowerStats() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .strength(null)
                .agility(null)
                .dexterity(null)
                .intelligence(null)
                .race(Race.HUMAN)
                .build();
    }

    public CreateHeroRequest createHeroRequestWithoutHeroAttributes() {
        return CreateHeroRequest.builder()
                .name("")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(null)
                .build();
    }
}
