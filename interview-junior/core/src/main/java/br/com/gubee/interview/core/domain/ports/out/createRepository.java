package br.com.gubee.interview.core.domain.ports.out;

import java.util.UUID;

public interface createRepository<T> {
    UUID create(T t);
}
