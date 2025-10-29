package br.com.gubee.interview.core.features.hero;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.domain.ports.in.DeleteService;
import br.com.gubee.interview.core.domain.ports.out.DeleteRepository;
import br.com.gubee.interview.core.domain.services.hero.DeleteHeroService;

@ExtendWith(MockitoExtension.class)
public class DeleteHeroServiceUnitTest {


    @Mock
    DeleteRepository deleteHeroRepository;

    DeleteService deleteHeroService;

    @BeforeEach
    public void setUp() {
        deleteHeroService = new DeleteHeroService(deleteHeroRepository);
    }
    
    @Test
        public void deleteByName() {
                String heroName = "Yan";

                doNothing().when(deleteHeroRepository).deleteByName(heroName);
                deleteHeroService.deleteByName(heroName);

                verify(deleteHeroRepository, times(1)).deleteByName(heroName);
        }

        @Test
        public void deleteById() {
                UUID heroId = UUID.randomUUID();

                doNothing().when(deleteHeroRepository).deleteById(heroId);
                deleteHeroService.deleteById(heroId);

                verify(deleteHeroRepository, times(1)).deleteById(heroId);
        }
}
