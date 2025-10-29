package br.com.gubee.interview.core.domain.services.hero;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.domain.ports.in.DeleteService;
import br.com.gubee.interview.core.domain.ports.out.DeleteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteHeroService implements DeleteService {

    private final DeleteRepository deleteHeroRepository;

    @Transactional
    @Override
    public void deleteById(UUID id) {
        deleteHeroRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByName(String name) {
        deleteHeroRepository.deleteByName(name);
    }
}
