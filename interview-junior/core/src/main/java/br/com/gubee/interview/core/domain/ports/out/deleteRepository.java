package br.com.gubee.interview.core.domain.ports.out;

import java.util.UUID;

public interface DeleteRepository {
    void deleteById(UUID id);
    void deleteByName(String name);
}
