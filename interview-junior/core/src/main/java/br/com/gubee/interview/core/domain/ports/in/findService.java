package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface findService<T> {

    T findById(UUID id);
    T findByName(String name);
}
