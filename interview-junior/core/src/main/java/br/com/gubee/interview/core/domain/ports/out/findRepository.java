package br.com.gubee.interview.core.domain.ports.out;

import java.util.UUID;

public interface findRepository<T> {
    T findById(UUID id);
    default T findByName(String name) {
        return null;
    }
}
