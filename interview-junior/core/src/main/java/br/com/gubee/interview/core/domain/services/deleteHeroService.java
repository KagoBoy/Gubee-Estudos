package br.com.gubee.interview.core.domain.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.domain.ports.in.deleteService;
import br.com.gubee.interview.core.domain.ports.out.deleteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class deleteHeroService implements deleteService {

    private final deleteRepository deleteHeroRepository;

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
