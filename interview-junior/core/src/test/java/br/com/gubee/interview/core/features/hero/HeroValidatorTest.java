package br.com.gubee.interview.core.features.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.model.request.CreateHeroRequest;

@ExtendWith(MockitoExtension.class)
public class HeroValidatorTest {

    private Validator validator;
    private Requests request = new Requests();

        @BeforeEach
        public void setUp() {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
        }

        @Test
        public void createWithoutPowerStats_ShouldReturnValidationMessage() {
                Set<ConstraintViolation<CreateHeroRequest>> violations = validator
                                .validate(request.createHeroRequestWithoutPowerStats());

                List<String> errorMessages = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .toList();
                assertTrue(errorMessages.contains("message.powerstats.strength.mandatory"));
                assertTrue(errorMessages.contains("message.powerstats.agility.mandatory"));
                assertTrue(errorMessages.contains("message.powerstats.dexterity.mandatory"));
                assertTrue(errorMessages.contains("message.powerstats.intelligence.mandatory"));

                assertEquals(4, violations.size());
        }

        @Test
        public void createWithoutHeroAttributes_ShouldReturnValidationMessage() {
                Set<ConstraintViolation<CreateHeroRequest>> violations = validator
                                .validate(request.createHeroRequestWithoutHeroAttributes());

                List<String> errorMessages = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .toList();
                assertTrue(errorMessages.contains("message.name.mandatory"));
                assertTrue(errorMessages.contains("message.race.mandatory"));
                assertTrue(errorMessages.contains("message.name.length"));

                assertEquals(3, violations.size());
        }

        @Test
        public void createRequestWithAllArguments() {
                Set<ConstraintViolation<CreateHeroRequest>> violations = validator
                                .validate(request.createHeroRequest());

                assertEquals(0, violations.size());
        }
}
