package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface DeleteService {

    void deleteById(UUID id);
    void deleteByName(String name);

}
