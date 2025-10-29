package br.com.gubee.interview.core.domain.ports.in;

import java.util.UUID;

public interface CompareService<T> {
 
    T compare(UUID id1, UUID id2);
}
