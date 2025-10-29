package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface UpdateService<T, Y> {
    Y updateById(T t, UUID id);
    Y updateByName(T t, String name);
}
