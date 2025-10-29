package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface deleteService {

    void deleteById(UUID id);
    void deleteByName(String name);

}
