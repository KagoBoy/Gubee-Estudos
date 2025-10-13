package br.com.gubee.interview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonResponse {

        private UUID hero1Id;
        private UUID hero2Id;
        private String hero_1;
        private String hero_2;
        private short strength;
        private short agility;
        private short dexterity;
        private short intelligence;

}
