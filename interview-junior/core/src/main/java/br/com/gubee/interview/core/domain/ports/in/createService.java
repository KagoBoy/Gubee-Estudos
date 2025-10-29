package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface CreateService<T> {
    UUID create(T t);
}
