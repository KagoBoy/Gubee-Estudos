package br.com.gubee.interview.core.domain.ports.out;

import java.util.UUID;

public interface updateRepository<T> {
    void updateById(T t, UUID id);
    default void updateByName(T t, String name) {
    }
}
